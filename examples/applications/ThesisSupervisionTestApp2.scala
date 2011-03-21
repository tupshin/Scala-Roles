/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package applications;

import collaborations.Person
import collaborations.ThesisSupervision
import scala.roles.Basics._

object ThesisSupervisionTestApp2 {
  def main(args : Array[String]) : Unit = {
    val jim = new Person{ val name = "Jim" }     // a master student
    val paul = new Person{ val name = "Paul" }   // a PhD student
    val peter = new Person{ val name = "Peter" } // a professor
    
    val master = new ThesisSupervision{}
    val phd = new ThesisSupervision{}
    
    (jim as master.student).work
    (paul as master.supervisor).advise
    
    (paul as phd.student).work
    (peter as phd.supervisor).advise
    (peter as phd.supervisor).grade
  }
}
