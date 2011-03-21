/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package applications

import collaborations.Person
import collaborations.ThesisSupervision
import scala.roles.Basics._

//TODO remove this
import scala.roles.internal._
import scala.roles._

object ThesisSupervisionTestApp  {

  def main(args: Array[String]) {
 
    trait Person2 extends Person {
      val x = 5
    }

    val klaus = new Person{ val name = "Klaus" } // a professor
    val peter = new Person{ val name= "Peter" }  // another professor
    val franz = new Person{ val name= "Franz" }  // a student
    
    val thesis = new ThesisSupervision{}
 
    val proxy = klaus -: thesis.student
    
    val p2 = new Person2{ val name = "p2" }
    println((p2 -: thesis.student).x)
    
    //TODO: why doesn't it work without giving the type (it's inferred to HasCore[Person])
    val p2AsStudent: HasCore[Person2] with Person2 = p2 -: thesis.student
    println(p2AsStudent.x)
    println(p2AsStudent.core.x)

    (franz as thesis.student).motivation = 90
    println( (franz -: thesis.student).name ) // returns "Franz"

    // in another context, Franz is the supervisor for Klaus (to show that it works..)
    val thesis2 = new ThesisSupervision{}
    (klaus -: thesis2.student).motivation = 40
    println((franz -: thesis2.supervisor).grade) // "bad"
     
    // question: should this work, i.e. should we restrict role changes?
    println((franz -: thesis.supervisor).grade)
        
    //("abc" -: thesis.student).motivation // type mismatch
    
    println("Franz -: Student: " + (franz -: thesis.student))
    
    /*println((franz -: thesis.student) == franz -: thesis2.supervisor)
    println((franz -: thesis.student) == (franz -: thesis.student))
    peter -: thesis.student
    val id = franz -: thesis.student
    println(id == (franz -: thesis.student))*/    

    println((franz -: thesis.student) == franz)
    println(franz == (franz -: thesis.student))
    println((franz -: thesis.student) == (franz -: thesis.supervisor))
  
  }
}
