/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package collaborations

import scala.roles._
import applications.MediatorTestRoles._

class FontDialogCollab(listB: ListBox, entryF: EntryField, radioB: RadioButton) extends StickyCollaboration {
  
  trait MyListBox extends Role[ListBox] {
    
    def updateDialog() = {
      entryF.setText(core.getSelection)
      if (core.getSelection == "def") radioB.enable
      else radioB.disable
    }
    
    addAfterCalls(updateDialog, "select(*)")
  }

  val myListBox = new MyListBox{}
  myListBox.bind(listB)

  def listBox = myListBox.proxy
    
}

