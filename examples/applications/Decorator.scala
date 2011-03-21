/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package applications

import collaborations._

object Decorator {
  trait VisualComponent {
    def draw
  }
  
  trait TextView extends VisualComponent {
    var text = "abc"
    override def draw = println(text)
  }

  def main(args: Array[String]) = {
    val text = new TextView{}
    val c = new ScrollCollab{}
    val scrollableText = text -: c.scrollable
    scrollableText.draw
    val scrollableBorderedText = scrollableText -: c.hasBorder
    scrollableBorderedText.draw
  }
  
}


