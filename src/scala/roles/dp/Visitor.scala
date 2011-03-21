/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package scala.roles.dp

trait Visitor extends TransientCollaboration {
	
  object element extends RoleMapper[AnyRef, Element] {
    def createRole = new Element{}
  }
  
  object visitor extends RoleMapper[VisitorImpl, Visitor] {
    def createRole = new Visitor{}
  }
  
  trait Visitor extends Role[VisitorImpl] {
    def visit[ElementType <: AnyRef](e: ElementType) = {
			val thisProxy = visitor.coreOf(this) -: this
      thisProxy.visitImpl[ElementType](e: ElementType)
    }
  }
  
  trait Element extends Role[AnyRef] {
    def accept(v: Visitor) = {
      val core = element.coreOf(this)
  	  type ElementType = core.type
      
      v.visit[ElementType](core.asInstanceOf[ElementType])
    }
    
    
  }
  
}

trait VisitorImpl {
  def visitImpl[ElementType <: AnyRef](e: ElementType)
}
