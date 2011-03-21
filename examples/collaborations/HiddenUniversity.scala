/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package collaborations

import scala.roles._
import scala.collection.mutable.HashSet
import scala.collection.mutable.HashMap
import scala.collection.Set

// TODO use implicit conversions 
class HiddenUniversity extends TransientCollaboration {
	
	val studentIds = new HashMap[Integer, Student]()
  var maxId = 0
	
	protected trait Student extends Role[Person] {
	  var supervisor: Professor = _
	}

  protected object student extends RoleMapper[Person, Student] {
    def createRole = new Student{}
  }
	
	protected trait Professor extends Role[Person] {
		val students = new HashSet[Student]()
  }

  protected object professor extends RoleMapper[Person, Professor] {
    def createRole = new Professor{}
  }
	
	def enroll(stud: Person) = {
    maxId = maxId + 1
		studentIds put (maxId, stud -: student)
		maxId
	}
	
	def supervise(prof: Person, stud: Person): Unit = {
		(stud -: student).supervisor = (prof -: professor)
		(prof -: professor).students += (stud -: student)
	}
	
	def getStudents(prof: Person): Iterable[Person] = {
    for {
      student <- (prof -: professor).students
    } yield student.core
	}
	
  def getStudent(id: Int): Option[Person] = {
  	studentIds.get(id) match {
      case Some(s)   => Some(s.core)
      case None      => None
    }
  }
}
