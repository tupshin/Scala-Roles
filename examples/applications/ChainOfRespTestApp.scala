/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package applications;

import scala.roles.dp._

object ChainOfRespTestApp {
  def main(args : Array[String]) : Unit = {
    val cor = new ChainOfResponsibility[XWidget]{}
    
    val b = new XButton{}
    val d = new XDialog{}
    
    (b -: cor.handler).successor = (d -: cor.handler)
    (b -: cor.handler).handleInChain
    
    println("a")
  }
}

trait XWidget extends Handler {
  def handle: Unit 
  def canHandle: Boolean
}

trait XButton extends XWidget {
  def handle = ""
  def canHandle = false
}

trait XDialog extends XWidget {
  def handle = println("this is a dialog")
  def canHandle = true
}
