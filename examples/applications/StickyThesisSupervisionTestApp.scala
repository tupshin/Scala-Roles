/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package applications

import collaborations.Person
import collaborations.StickyThesisSupervision._

object StickyThesisSupervisionTestApp {

  trait SpecialPerson extends Person {
    def x = 23
  }
  
  def main(args : Array[String]) : Unit = {
    val klaus = new Person{ val name = "Klaus" }  // a professor
    val peter = new Person{ val name = "Peter" }  // another student
    val franz = new SpecialPerson{ val name = "Franz" }  // a student
    
    val s = new StickyThesisSupervision(klaus, peter)
   
    s.professor.advise
    println(s.student.wisdom)
    
    println(s.professor.writeLetter)
    
    s.student.bind(franz)
    println(franz.title + " " + franz.name)
    println(s.student.wisdom)
    
    println(s.professor.writeLetter)
    
    s.professor.awardDiploma
    
    println(franz.title + " " + franz.name)
    
    // now, the student is a SpecialPerson
    val s2 = new StickyThesisSupervision(klaus, franz)
    println(s2.student.x)
  }
}
