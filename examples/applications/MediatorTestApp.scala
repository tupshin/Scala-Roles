/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package applications

import collaborations.FontDialogCollab

object MediatorTestAppRoles {
  import MediatorTestRoles._

  def main(args: Array[String]) = {
    val lb = new ListBox{}
    val ef = new EntryField{}
    val rb = new RadioButton{}

    val mediator = new FontDialogCollab(lb, ef, rb)
    
    mediator.listBox.select(1)
    mediator.listBox.select(0)
    mediator.listBox.select(2)
  }
}

object MediatorTestRoles {
  
  trait ListBox {
    private var selection = "abc"
    def select(item: Int) = {
      println("selected: " + item)
      if (item == 0) selection = "abc"
      else if (item == 1) selection = "def"
      else selection = "xyz"
    }
    def getSelection = selection
  }

  trait EntryField {
    def setText(t: String) = println("text set to " + t)
  }

  trait RadioButton {
    def enable = println("enabled")
    def disable = println("disabled")
  }

}

object MediatorTestAppNoRoles {
  import MediatorTestNoRoles._

  def main(args: Array[String]) = {
    val fontDirector = new FontDialogDirector{}
    fontDirector.lb.select(1)
    fontDirector.lb.select(0)
    fontDirector.lb.select(2)
  }
}

object MediatorTestNoRoles {
  trait Widget {
    val director: FontDialogDirector
    def changed = director.widgetChanged(this)
  }
  
  trait ListBox extends Widget {
    private var selection = "abc"
    def select(item: Int) = {
      println("selected: " + item)
      if (item == 0) selection = "abc"
      else if (item == 1) selection = "def"
      else selection = "xyz"
      changed
    }
    def getSelection = selection
  }

  trait EntryField extends Widget {
    def setText(t: String) = println("text set to " + t)
  }

  trait RadioButton extends Widget {
    def enable = println("enabled")
    def disable = println("disabled")
  }

  trait FontDialogDirector {
    val fdd = this
    val lb = new ListBox{ override val director = fdd }
    val ef = new EntryField{ override val director = fdd }
    val rb = new RadioButton{ override val director = fdd }
    
    def widgetChanged(w: Widget) = 
      if (w == lb) {
        ef.setText(lb.getSelection)
        if(lb.getSelection == "def") rb.enable
        else rb.disable
      }
  }

}
