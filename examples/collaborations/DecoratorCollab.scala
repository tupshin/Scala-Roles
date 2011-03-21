/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package collaborations

import applications.Decorator._
import scala.roles._

trait ScrollCollab extends TransientCollaboration {
  
  trait Scrollable extends Role[TextView] {
    def draw = {
      core.draw
      println("--------SCROLLBAR-------")
    }
  }

  val scrollable = new Scrollable{}
  
  trait HasBorder extends Role[TextView] {
    def draw = {
      println("*************************")
      core.draw
      println("*************************")
    }
  }

  val hasBorder = new HasBorder{}
  
}

