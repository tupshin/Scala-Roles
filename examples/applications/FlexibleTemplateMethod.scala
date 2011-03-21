/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package applications

import scala.roles._

object FlexibleTemplateMethod {

  trait Abstract {
    def m(s: String) = 3 + n
    def n: Int
  }

  trait Concrete extends Abstract {
    override def n = 5
  }

  // allow using roles without collabs?
  trait MyCollab extends TransientCollaboration {
    val otherConcrete = new OtherConcrete{}
    trait OtherConcrete extends Role[Abstract] with Abstract {
      override def n = 7
    }
  }

  def main(args: Array[String]) = {
    val myConcrete = new Concrete{}

    println(myConcrete.m("A"))

    val c = new MyCollab{}

    println((myConcrete -: c.otherConcrete).m("B"))
  }

}
