/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package applications

import scala.roles._

object CompoundTypes {

  trait A { def m = 23 }
    
  trait B { def n = 42 }

  trait MyCollab extends TransientCollaboration {
    val r = new R{}
    trait R extends Role[A] {
      def blubb = 1
    }
  }
  
  def main(args: Array[String]) = {
    val c = new MyCollab {}
    
    val x = new A with B {}

    println((x -: c.r).blubb)

  }

}
