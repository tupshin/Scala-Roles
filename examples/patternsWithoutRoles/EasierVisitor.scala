/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package patternsWithoutRoles

import scala.collection.mutable.HashSet
import util.PerformanceAnalysis._

object EasierVisitor {
  def main(args : Array[String]) : Unit = {
    val company = new Company { val name = "foo" }
    val joe: Manager = new Manager { val name = "Joe"; var salary = 200 }
    val paul: Manager = new Manager { val name = "Paul"; var salary = 300 }
    val tom: Employee = new Employee { val name = "Tom"; var salary = 100 }
    val dep1 = new Department { val name = "dep1"; var manager = joe }
    val dep2 = new Department { val name = "dep2"; var manager = paul }
    
    company.departments += dep1
    dep1.subunits += dep2
    dep2.subunits += tom
    
    val salaryVis = new SalaryVisitor {}
    val printVis = new PrintVisitor {}
    
    measureTime {
      printVis.visit(company)
      println
      salaryVis.visit(company)
      printVis.visit(company)
      println
    }
  }
  
  trait SalaryVisitor {
    def visit[ElementType <: AnyRef](e: ElementType) {
      // match on dynamic type of e
      e match {
        case e: Company      => e.departments.foreach(visit)
        case e: Department   => e.manager.salary += 10; e.subunits.foreach(visit)
        case e: Employee     => e.salary += 10
        case _               => println("did not match: element has type " + e.getClass)
      }
    }        
  }
  
  trait PrintVisitor {
    def visit[ElementType <: AnyRef](e: ElementType) { 
      e match {
        case e: Company      => println("Company " + e.name + " with departments:"); e.departments.foreach(visit)
        case e: Department   => println("Department " + e.name + " with Manager:"); visit(e.manager); println("..and subunits:"); e.subunits.foreach(visit) 
        case e: Employee     => println("Employee " + e.name + ", salary: " + e.salary)
        case _               => println("did not match: element has type " + e.getClass)
      }
    }
  }
  
  trait Company {
    val name: String
    val departments = new HashSet[Department]
  }
  
  trait SubUnit

  trait Department extends SubUnit {
    val name: String
    var manager: Manager
    val subunits = new HashSet[SubUnit]
  }

  trait Employee extends SubUnit {
    val name: String
    var salary: Int
  }

  type Manager = Employee
}
