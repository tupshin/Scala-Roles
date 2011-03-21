/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */


package patternsWithoutRoles

class HelpHandler(var successor: HelpHandler, var topic: String) {
  def hasHelp = topic != ""
  def handleHelp: Unit = if (successor != null) successor.handleHelp
}

abstract class Widget(var parent: Widget, var topic2: String) extends HelpHandler(parent, topic2) {
  def size: (Int,Int)
}

class Button(var p: Widget, var t: String) extends Widget(p, t) {
  override def size = (4,1)
  
  override def handleHelp = 
    if (hasHelp) {
      // offer help on 
      // the button
    } else {
      super.handleHelp
    }
}

class Dialog(var h: HelpHandler, var topc: String) extends Widget(null, topc) {
  successor = h
  
  override def size = (20,20)
  
  override def handleHelp = 
    if (hasHelp) {
      // offer help on 
      // the dialog
    } else {
      super.handleHelp
    }
}

class Applictn(var t: String) extends HelpHandler(null, t) {
  def start = {}  
  def stop = {}
  override def handleHelp = {
    // show list with help topics
  }
}
