/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package patternsWithoutRoles

import scala.collection.mutable.HashSet

trait BookS {
  var status = "available"

  def borrow = {
    status = "borrowed"
    notifyObservers
  }
  
  def returnIt = {
    status = "available"
    notifyObservers    
  }
  
  val observers = new HashSet[LibraryO]()
  
  def addObserver(o: LibraryO) = observers += o
  
  def removeObserver(o: LibraryO) = observers -= o
  
  def notifyObservers = observers.foreach(_.update(this))
}

trait LibraryO {
  val availableBooks = new HashSet[BookS]()
  val borrowedBooks = new HashSet[BookS]()
  
  def update(b: BookS) = {
    b.status match {
      case "available"   => availableBooks += b; borrowedBooks -= b
      case "borrowed"    => availableBooks -= b; borrowedBooks += b
    }      
  }
}
