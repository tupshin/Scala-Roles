/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package scala.roles.dp;

/*

// Problem: Chain of Resp. seems to become more complicated with roles than without...

trait ChainOfResponsibility2[Request] extends MultiCollaboration {

  val handlingMethod: Method

  val canHandle: (Request, Handler) => Boolean

  def element[Handler](p: Request => Boolean, next: Handler) = new HandlerRole{...}

  object handlerMapper extends RoleMapper[Handler, HandlerRole] {
    def createRole = new HandlerRole{ val succ = next }
  }
  
  trait HandlerRole extends Role[Handler] = {
    val succ: Handler

    
    
  }
  
}
*/
