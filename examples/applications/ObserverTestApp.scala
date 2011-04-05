/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package applications;

import scala.roles.dp._
import scala.roles.Basics._

object ObserverTestApp {
  def main(args : Array[String]) : Unit = {
    val b1 = new Book{}
    val b2 = new Book{}
    val l = new Library{}
    
    /*
    trait SubjectStateChangers extends Book {
      override def borrow = {}
      override def returnIt(msg: String) = {}
    }
    */
    
    //val o = new ObserverCollab[Book]("borrow()", "returnIt(java.lang.String,int)", "status")    
    val o = new ObserverCollab[Book]("status")    
 
    val obsB1 = b1 as o.subject
    obsB1.addObserver(l)
    
    obsB1.borrow
    
    b1.returnIt("Thanks")
    obsB1.returnIt("Thanks a lot")
    obsB1.returnIt("Thanks a lot", 3)

    //obsB1.status = "borrowed"
    
  }
}

trait Book {
  private var status = "available"
  def borrow = { status = "borrowed" }
  def returnIt = { status = "available" }
  def returnIt(msg: String) = { status = "available" }
  def returnIt(msg: String, delay: Int) = { status = "available" }
  def turnPage = {}
}

trait Library extends Observer[Book] {
  def update(b: Book) = {
    println("Got notification that " + b + " changed...")
  }
}
