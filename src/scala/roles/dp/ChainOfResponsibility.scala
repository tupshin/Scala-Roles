/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package scala.roles.dp;

trait ChainOfResponsibility[HandlerPlayer <: Handler] extends TransientCollaboration {
  
  object handler extends RoleMapper[HandlerPlayer, HandlerRole] {
    def createRole = new HandlerRole{}
  }
  
  trait HandlerRole extends Role[HandlerPlayer] {
    var successor: HandlerRole = _
  
    def handleInChain: Unit = {
      val thisAsProxy = handler.coreOf(this) -: this
      
      if (thisAsProxy.canHandle) 
        thisAsProxy.handle
      else
        successor.handleInChain
    }
  }
  
  
  
}

trait Handler {
  def handle
  def canHandle: Boolean
}
