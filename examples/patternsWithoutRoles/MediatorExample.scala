/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package patternsWithoutRoles

abstract class DialogDirector {
  def showDialog
  def widgetChanged(w: MWidget)
}

abstract class MWidget(val d: DialogDirector) {
  def changed() = d.widgetChanged(this)
  def handleMouse(e: MouseEvent) 
}

class MListBox(override val d: DialogDirector) extends MWidget(d) {
  def getSelection: String = ""
  def setList(listItems: List[String]) = {}
  override def handleMouse(e: MouseEvent) = {}
}

class MEntryField(override val d: DialogDirector) extends MWidget(d) {
  var text: String = ""
  def handleMouse(e: MouseEvent) = {}
}

class MButton(override val d: DialogDirector) extends MWidget(d) {
  var text: String = ""
  def handleMouse(e: MouseEvent) = changed()
}

class FontDialogDirector extends DialogDirector {
  val okButton = new MButton(this)
  val cancelButton = new MButton(this)
  val fontList = new MListBox(this)
  val fontName = new MEntryField(this)
  
  def widgetChanged(w: MWidget) = {
    if (w == fontList) {
      fontName.text = fontList.getSelection
    } else if (w == okButton) {
      // ... 
    } else if (w == cancelButton) {
      // ...
    }
        
  }
  
  def showDialog = {}
}

class MouseEvent
