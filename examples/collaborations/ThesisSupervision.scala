/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package collaborations

import scala.roles._

trait ThesisSupervision extends TransientCollaboration {

  val supervisor = new Supervisor{}
  val student = new Student{}

  trait Supervisor extends Role[Person] {
    def advise = student.motivation += 5
    def grade = if (student.wisdom > 80) "good" else "bad"
  }

  trait Student extends Role[Person] {
    var motivation = 50
    var wisdom = 0
    def work = wisdom += motivation/10
  }
}

