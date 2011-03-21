/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package patternsWithoutRoles

import scala.collection.mutable.HashSet
import util.PerformanceAnalysis._

object VisitorExample {
  def main(args: Array[String]) {
    val company = new Company { val name = "foo" }
    val joe: Manager = new Manager { val name = "Joe"; var salary = 200 }
    val paul: Manager = new Manager { val name = "Paul"; var salary = 300 }
    val tom: Employee = new Employee { val name = "Tom"; var salary = 100 }
    val dep1 = new Department { val name = "dep1"; var manager = joe }
    val dep2 = new Department { val name = "dep2"; var manager = paul }
    
    company.departments += dep1
    dep1.subunits += dep2
    dep2.subunits += tom
    
    val printVis = new PrintVisitor{}
    val salaryVis = new SalaryVisitor{}
    
    measureTime {
      company.accept(printVis)
      company.accept(salaryVis)
      company.accept(printVis)
    }
  }
    
  trait Company {
    val name: String
    val departments = new HashSet[Department]
    def accept(v: Visitor) = v.visitCompany(this)
  }

  trait SubUnit {
    def accept(v: Visitor)
  }

  trait Department extends SubUnit {
    val name: String
    var manager: Manager
    val subunits = new HashSet[SubUnit]
    def accept(v: Visitor) = v.visitDepartment(this)
  }

  trait Employee extends SubUnit {
    val name: String
    var salary: Int
    def accept(v: Visitor) = v.visitEmployee(this)
  }

  type Manager = Employee
  
  abstract class Visitor {
    def visitCompany(c: Company)
    def visitDepartment(d: Department)
    def visitEmployee(e: Employee)
  }
  
  class SalaryVisitor extends Visitor {
    override def visitCompany(c: Company) {
      c.departments.foreach(_.accept(this))
    }
    
    override def visitDepartment(d: Department) {
      d.manager.salary += 10
      d.subunits.foreach(_.accept(this))
    }
    
    override def visitEmployee(e: Employee) {
      e.salary += 10
    }
  }
  
  class PrintVisitor extends Visitor {
    override def visitCompany(c: Company) {
      println("Company " + c.name + " with departments:")
      c.departments.foreach(_.accept(this))
    }
    
    override def visitDepartment(d: Department) {
      println("Department " + d.name + " with Manager:")
      d.manager.accept(this)
      println("..and subunits:")
      d.subunits.foreach(_.accept(this))
    }
    
    override def visitEmployee(e: Employee) {
      println("Employee " + e.name + ", salary: " + e.salary)
    }
  }
}
