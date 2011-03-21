/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package scala.roles.internal

import scala.collection.mutable.HashMap
import java.lang.reflect.{InvocationHandler => JavaInvocationHandler, Method, InvocationTargetException, Proxy, Modifier}
import scala.roles.internal._

class InvocationHandler(core: AnyRef, role: AnyRef, sharedIdentities: Boolean, beforeCalls: InvocationHandler.CallMapping, afterCalls: InvocationHandler.CallMapping) extends JavaInvocationHandler {
 
  import InvocationHandler._
  
  type AnyClass = Class[T] forSome { type T }
  
  def invoke(proxy: AnyRef, method: Method, args: Array[Object]): AnyRef = {
    def specialNames(m: Method): String = {
      val pts = m.getParameterTypes
      if (m.getName == "toString" && pts.length == 0)
        "toString"
      else if (m.getName == "equals" && pts.length == 1 &&
        pts(0) == classOf[Object]) 
        "equals"
      else if (m.getName == "hashCode" && pts.length == 0)
        "hashCode"
      else if (m.getName == "role" && pts.length == 0)
        "role"
      else 
        null
    }
    
    if (sharedIdentities) {
      specialNames(method) match {
        case "toString"   => core.toString
        case "equals"     => 
        if (args(0).isInstanceOf[Proxy]) {
          val other = args(0).asInstanceOf[Proxy with HasCore[_]]
          val otherCore = other.core 
          boolean2Boolean((core == otherCore)) 
        } else 
        boolean2Boolean(core == (args(0)))
        case "hashCode"   => int2Integer(core.hashCode)
        case "role"       => role
        case _            => invokeNormalMethod(method, args, proxy)
      }
    } else {
      invokeNormalMethod(method, args, proxy)
    }
  }
  
  private def compareTypes(s: Seq[(AnyClass, AnyClass)]): Boolean = {
    s.foreach(tuple => if (tuple._1.getName != tuple._2.getName) return false)
    true
  }
  
  /**
    Compares two method and returns true iff they have the same name, number of parameters and parameter types.
  */
  protected def compare(m1: Method, m2: Method): boolean = {
    //print("   comparing "+m1.getName+" with "+m2.getName+" : ")
    // name
    if (m1.getName != m2.getName) return false
    // parameter types
    val types1 = m1.getParameterTypes
    val types2 = m2.getParameterTypes
    //print(compareTypes(types1 zip types2)+", "+(types1.size == types2.size)+" -> ")
    if (types1.size != types2.size
     || !compareTypes(types1 zip types2)) return false
    //println("TRUE")
    true
  }
  
  /**
    Compares two method and returns true iff they have the same name 
    and selfM prepends the parameter list of normalM with a 'self' parameter
    of type selfT. I.e. selfM is a variant of normalM that allows to pass
    a value for 'this' (aka 'self') to be used in the method implementation.
  */
  protected def compareSelfMethod(normalM: Method, selfM: Method, selfT: String): boolean = {
    // name
    if (normalM.getName != selfM.getName) return false
    // parameter types
    val types1 = normalM.getParameterTypes
    val types2 = selfM.getParameterTypes
    if (types1.size + 1 != types2.size
     || !compareTypes(types1 zip types2.drop(1))
     || types2(0).getName != selfT) return false
    true
  }
  
  protected def invokeNormalMethod(method: Method, args: Array[Object], proxy: AnyRef): AnyRef = {
    def delegate(cl: ClassLoader, cls: AnyClass, m: Method, args: Array[Object]): Option[Object] =
      findImplementation(cl, cls.getInterfaces.apply(0), m) match {
        case Some(mImpl) => Some(callWithSelf(mImpl, args))
        case None        => None
      }
    
    def findImplementation(cl: ClassLoader, itf: AnyClass, m: Method): Option[Method] = {
      //println("checking... "+itf.getName)
      itf.getMethods.find(x => compare(m, x)) match {
        case Some(foundM) =>
          var traitImpl: AnyClass = null
          try { traitImpl = cl.loadClass(itf.getName+"$class") } 
          catch { case e: ClassNotFoundException => {
            //println("classfile not found: "+itf.getName+"$class")
            itf.getInterfaces.foreach(i => { 
              findImplementation(cl, i, m) match {
                case Some(foundM) => return Some(foundM)
                case None         =>
              }
            })
            return None
          }}
          traitImpl.getMethods.find(mImpl => compareSelfMethod(m, mImpl, itf.getName)) match {
            case Some(x) if((x.getModifiers & Modifier.ABSTRACT) == 0) => return Some(x)
            case _ =>
              //println(m.getName+": found no method or abstract one in "+traitImpl.getName)
              itf.getInterfaces.foreach(i => { 
                findImplementation(cl, i, m) match {
                  case Some(foundM) => return Some(foundM)
                  case None         =>
                }
              })
              return None
          }

        case _ => None
      }  
    }
    
    def callWithSelf(m: Method, args: Array[Object]) = {
      val newArgs = if (args != null) { /*println(args.size);*/ Array(proxy)++args } else Array(proxy)
      //println("invoking... " + m)
      m.invoke(null, newArgs:_*)
    }

    try {
      List(role, core).foreach(d => {
        val methods = d.getClass.getMethods
        d.getClass.getMethods.foreach(foundMethod => {
          if (compare(method, foundMethod)) {
            val mId = methodId(foundMethod)
            applySlotMethods(beforeCalls, mId)
            var r: AnyRef = null
            delegate(d.getClass.getClassLoader, d.getClass, foundMethod, args) match {
              case Some(x) => r = x
              case None    => 
                // if delegation not possible, forward the call (should only happen for setters and getters)
                r = foundMethod.invoke(d, args:_*)
            }
            applySlotMethods(afterCalls, mId)
            return r
          }
        })
      })
   
      println("ERROR: method " + method + " not found in " + role + " or " + core) 
    } catch {
      case e: InvocationTargetException => throw e.getTargetException() 
    }
  }

  private def applySlotMethods(callMapping: CallMapping, methodId: String) = {
    callMappings(callMapping, methodId) match {
      case Some(l) => l.foreach(_.apply)
      case None    =>
    } 
  }
 
  /**
   Get the list of functions to invoke when the method described by 'methodId' is called.
  */
  private def callMappings(mapping: CallMapping, methodId: String): Option[List[() => Unit]] = {
    if (!methodId.contains("(")) mapping.get(methodId)
    else mapping.get(methodId) match {
      case Some(l) => Some(l)
      case None    => // try with wildcard
        mapping.get(wildCarded(methodId)) match {
          case Some(l) => Some(l)
          case None    => // try encoding of private fields: "surroundingClass$$fieldName_$eq(fieldType)"
            mapping.elements.find(e =>
                e._1.contains("_$eq") && wildCarded(methodId).contains("$$"+ e._1.substring(0, e._1.indexOf('(')) +"(*)")) match {
              case Some(e: (String, List[() => Unit])) => Some(e._2)
              case None    => None
            }
        }
    }
  }

  private def wildCarded(methodId: String) = methodId.substring(0, methodId.indexOf('(')+1)+"*"+")"
  
  private def methodId(m: Method) = {
    val sb = new StringBuilder(m.getName)
    sb.append("(")
    m.getParameterTypes.foreach(t => {
        sb.append(t.getName)
        sb.append(",")
      })
    if (sb.charAt(sb.length-1) == ',') sb.delete(sb.length-1, sb.length)
    sb.append(")")
    sb.toString
  }
}

object InvocationHandler {
  type CallMapping = HashMap[String, List[() => Unit]] 
}
