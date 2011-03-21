/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package collaborations

import scala.roles._

object StickyThesisSupervision {

  class StickyThesisSupervision[ProfP <: Person, StudP <: Person](profP: ProfP, studP: StudP) extends StickyCollaboration {
      
    trait Professor extends Role[ProfP] {
      def advise = studentRole.wisdom += 1
      def grade = if (studentRole.motivation > 80) "good" else "bad"
      def writeLetter = studentRole.core.name + " is very " + grade
      def awardDiploma = studentRole.core.title = "Dipl."
    }

    trait Student extends Role[StudP] {
      var motivation = 100
      var wisdom = 0
    }

    val professorRole = new Professor{}
    val studentRole = new Student{}

    professorRole.bind(profP)
    studentRole.bind(studP)
    
    def professor = professorRole.proxy
    def student = studentRole.proxy
  }
}
