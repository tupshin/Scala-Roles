/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package applications

import scala.roles._

object SetterGetter {
  
  trait A {
    var state = 23
  }
  
  trait MyCollab extends TransientCollaboration {
    val r = new R{}
    trait R extends Role[A] {
      def blubb = core.state
      //def blubb = core
      //def blubb = 17
    }
  }
  
  def main (args: Array[String]) = {
    val c = new MyCollab{}
    
    val a = new A{}

    println((a -: c.r).blubb)
  }
  
}
