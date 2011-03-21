/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package applications

import scala.roles._
import scala.roles.Basics._

object Proxy {

  trait Data {
    private var v: Int = 5
    def getValue= v
  }

  trait RemoteCollab extends TransientCollaboration {
    trait Remote extends Role[Data] {
      def getValue = { println("getting v from somewhere..."); 23 }
    }
    val remote = new Remote{}
  }
  

  def main(args: Array[String]) = {
  
    val element = new Data{}

    val c = new RemoteCollab{}
    import c._

    (element as remote).getValue

  }
  
}
