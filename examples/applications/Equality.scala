/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package applications

import scala.roles._
import scala.roles.Basics._

object Equality {

  trait A { def m = 23 }
    
  trait MyCollab extends TransientCollaboration {
    
    sharedIdentities = false
    
    val r1 = new R{}
    val r2 = new R{}
    trait R extends Role[A] {
      def blubb = 1
    }
  }
  
  def main(args: Array[String]) = {
    val c = new MyCollab {}
    
    val a = new A{}

    println(a == (a as c.r1))
    println((a as c.r1) == a)
    println((a as c.r1) eq (a as c.r1))
    println((a as c.r1) == (a as c.r2))

  }

}
