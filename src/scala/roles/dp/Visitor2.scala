/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package scala.roles.dp;

import scala.roles.TransientCollaboration

trait Visitor2 extends TransientCollaboration {

  // to be implemented by concrete visitor collaboration
  def createVisitor: AnyRef
  def traverse(el: AnyRef): Seq[AnyRef] 
  
  object element extends RoleMapper[AnyRef, Element] {
    def createRole = new Element{}
  }
  
  object visitor {
    def visit[ElementPlayer <: AnyRef](el: ElementPlayer) = {
      // perform operation by searching for visitElementPlayer method
      // if none found, do nothing
      val realVisitor = createVisitor
      realVisitor.getClass.getMethods.filter(_.getName.contains("visit")).foreach(m => { // TODO better filtering of methods
        val paramTypes = m.getParameterTypes
        if (paramTypes.length != 1) 
          // TODO runtime exceptions...
          throw new Exception("All visitXYZ methods should have exactly one parameter, but " + m.getName + " has " + paramTypes.foreach(_.getName))
        if (paramTypes(0).isAssignableFrom(el.getClass)) {
          m.invoke(realVisitor, Array[AnyRef](el):_*)
        }
      })
    }
  }
  
  trait Element extends Role[AnyRef] {
    def accept: Unit = {
      val core = element.coreOf(this)
      type ElementPlayer = core.type
      visitor.visit[ElementPlayer](core.asInstanceOf[ElementPlayer]) // TODO is this cast necessary?
      val children = traverse(core)
      children.foreach(child => {
        (child -: element).accept
      })
      //traverse(core).foreach(child => visitor.visit(child))
    }
  }
  
}
