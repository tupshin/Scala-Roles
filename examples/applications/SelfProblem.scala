/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package applications

import scala.roles._

object SelfProblem {

  trait A {
    def m(i: Int) = println("A.m")
  }

  trait B {
    def m(s: String) = println("B.m")
  }

  trait C extends A with B {
    def n
  }

  trait D extends C { 
    override def n = m(3)
  }

  trait MyCollab extends TransientCollaboration {
    val deco = new Deco{}
    trait Deco extends Role[C] {      // TODO override core type??
      def m(i: Int) = println("Deco.m")
      //def m(s: String) = println("Deco.m")
      //def m(i: Int, s: String) = println("Deco.m")
    }
  }
  
  def main(args: Array[String]) = {
    val c = new MyCollab{}

    val d = new D{}

    d.n
    (d -: c.deco).n
  }
  
}
