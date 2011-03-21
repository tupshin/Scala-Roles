/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package scala.roles.internal

import scala.collection.mutable.HashSet
import scala.collection.mutable.HashMap
import java.lang.reflect.{InvocationHandler => JavaInvocationHandler, Method, InvocationTargetException, Proxy => DynProxy}
import InvocationHandler._

private[roles] trait Collaboration {
 
  // determines whether obj and (obj as role) should have the same object identity
  protected var sharedIdentities = true
  
  trait AbstractRole[Player <: AnyRef] extends HasCore[Player] {
    // upper bound for type of proxy
    type Proxy = this.type with Player

    type AnyClass = Class[T] forSome { type T }

    protected var myCore: Player = _

    // core object -> proxy
    val proxyCache = new HashMap[Player, AnyRef]()

    def core = myCore

    def role: this.type = this

    def invHandler(core: Player) = new InvocationHandler(core, this, sharedIdentities, beforeCalls, afterCalls)
    
    protected def createProxy(core: Player, handler: JavaInvocationHandler, roles: AnyRef*)  = { 
      
      def addItf(obj: AnyRef, itfs: HashSet[AnyClass]): Unit = {
        def cannotHandle(cls: AnyClass) = {
          print("ERROR: " + cls + " is not a trait")
        }
      
        // this could be much more elegant and robust with a Scala reflection library...
        
        // 1) a proxy, i.e. a role-playing role   
        if (obj.isInstanceOf[DynProxy]) {
          itfs ++= obj.getClass.getInterfaces
          return
        }
        
        // 2) an interface
        val cls = obj.getClass
        if (cls.isInterface) { 
          itfs += cls
          return
        }

        // 3) a trait or something else
        val itfs_ = cls.getInterfaces
        if (itfs_.exists(_.getName == "scala.ScalaObject"))
          cannotHandle(cls)
        itfs ++= itfs_ 
      }

      val itfs = new HashSet[AnyClass]
      itfs += classOf[HasCore[Player]]
      roles.foreach(addItf(_, itfs))
      addItf(core, itfs)
      
      DynProxy.newProxyInstance(core.getClass.getClassLoader, itfs.toArray, handler)
    }

    private[roles] def playedBy(core: Player) = {
      myCore = core
      proxyCache.get(core) match {
        case Some(p) => p
        case None    => val p = createProxy(core, invHandler(core), this)
                        proxyCache.put(core, p)
                        p
      }
    }
  
    val beforeCalls = new CallMapping()
    val afterCalls = new CallMapping()

    def addBeforeCalls(code: () => Unit, invokers: String*) =
      addSlotMethods(beforeCalls, code, invokers:_*)

    def addAfterCalls(code: () => Unit, invokers: String*) =
      addSlotMethods(afterCalls, code, invokers:_*)

    private def addSlotMethods(callMapping: CallMapping, code: () => Unit, invokers: String*) = {
    invokers.foreach(i => {
        var invoker = i
        if (!invoker.contains("("))
          invoker = invoker+"_$eq(*)"
        callMapping.update(invoker, code :: (callMapping.get(invoker) match {
            case Some(l) => l
            case None    => Nil }))
      })
    }
  }

  trait TransientRole[Player <: AnyRef] extends AbstractRole[Player] {
    // -: reads 'as'
    def -:(core: Player): this.type with HasCore[core.type] with core.type = {
      playedBy(core).asInstanceOf[this.type with HasCore[core.type] with core.type]
    }
  }

  trait StickyRole[Player <: AnyRef] extends AbstractRole[Player] {
    var proxy: Proxy = _
    
    def bind(core: Player): Unit = {
      proxy = playedBy(core).asInstanceOf[Proxy]
    }
  } 

  trait AbstractRoleMapper[Player <: AnyRef, RoleType <: AbstractRole[Player]] {
    
    type Proxy = RoleType with Player

    private[Collaboration] val core2role = new HashMap[Player, RoleType]()


    private[Collaboration] val role2core = new HashMap[RoleType, Player]()
    
    // abstract factory method
    protected def createRole: RoleType
    
    def -:(core: Player): RoleType with HasCore[core.type] with core.type = {
      core2role.get(core) match {
        case Some(role)  => (role playedBy core).asInstanceOf[RoleType with HasCore[core.type] with core.type]
        case None     => {
          val role = createRole
          core2role.put(core, role)
          role2core.put(role, core)
          (role playedBy core).asInstanceOf[RoleType with HasCore[core.type] with core.type]
        }
      }
    }

    def transfer(oldCore: Player, newCore: Player) = {
      val role = roleOf(oldCore)
      unbind(role, oldCore)
      core2role.put(newCore, role)
      role2core.put(role, newCore)
    }
   
    // these shouldn't be public, but (ideally) only be visible in subclasses of Collaboration
    def roleOf(c: Player): RoleType = core2role.get(c) match {
      case Some(r)  => r
      case None     => throw new Exception("cannot find role of core " + c+ " with type "+ c.getClass.getName)
    }
    
    def coreOf(r: RoleType) = role2core.get(r) match {
      case Some(b)  => b
      case None     => throw new Exception("cannot find core of role " + r+ " with type "+r.getClass.getName)
    }

    def unbind(r: RoleType, b: Player): Unit = {
      core2role -= b
      role2core -= r
    }

    def unbind(proxy: Proxy): Unit = unbind(roleOf(proxy.core), proxy.core)

    def proxy(r: RoleType) = {
      val c = coreOf(r)
      r.playedBy(c).asInstanceOf[Proxy]
    }

    def proxy(c: Player) = {
      val r = roleOf(c)
      r.playedBy(c).asInstanceOf[Proxy]
    }
    
  }

}

trait HasCore[+Player] { def core: Player }
