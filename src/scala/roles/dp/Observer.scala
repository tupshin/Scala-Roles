/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package scala.roles.dp

import scala.collection.mutable.HashSet
import scala.roles.TransientCollaboration

/*
 A reusable implementation of the Observer pattern...
 */
class ObserverCollab[SubjectPlayer <: AnyRef](subjectStateChangers: String*) extends TransientCollaboration {
  
  object subject extends RoleMapper[SubjectPlayer, SubjectR] {
    def createRole = new SubjectR{}
  }
  
  trait SubjectR extends Role[SubjectPlayer] {
    private[this] val observers = new HashSet[Observer[SubjectPlayer]]()
    
    //type Observer = { def update(s: SubjectPlayer) }
    
    def addObserver(o: Observer[SubjectPlayer]) = {
      observers += o
    }
    
    def removeObserver(o: Observer[SubjectPlayer]) = {
      observers -= o 
    }
    
    def notifyObservers(): Unit = { 
      if (cycleCheck && inCycle.contains(core)) { println("found cycle with core "+core); return }
      if (cycleCheck) inCycle += core
      observers.foreach(_.update(core)) 
    }

    addAfterCalls(notifyObservers, subjectStateChangers:_*)
  }      

  var cycleCheck = false
  val inCycle = new HashSet[SubjectPlayer]()
  def cycleReset = { println("reset"); inCycle.clear }
 
}

trait Observer[SubjectPlayer] {
  def update(s: SubjectPlayer)
}


