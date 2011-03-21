/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package applications

import scala.roles._

object RefinementTypes {

  trait A {
    var state = 23
    def f = 1
  }
  
  trait MyCollab extends TransientCollaboration {
    val r = new R{}
    trait R extends Role[A] {
      def blubb = core.f + core.state
    }
  }
  
  def main (args: Array[String]) = {
    val c = new MyCollab{}
    
    val a = new A{
      def tralala = 7
    }

    println((a -: c.r).blubb)
    // known issue: causes exception, since refinement types have no (Java) interface...
    println((a -: c.r).tralala)
  }
  



}
