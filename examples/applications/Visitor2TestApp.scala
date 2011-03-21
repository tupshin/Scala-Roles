/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package applications;

import applications.CompanyDataStructure._
import collaborations.CompanyVisitors._
import scala.collection.mutable.HashSet

object Visitor2TestApp {
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
    
    val salVis = new SalaryVisitor{}
    val printVis = new PrintVisitor{}
    
    (company -: printVis.element).accept
    (company -: salVis.element).accept
    (company -: printVis.element).accept
    
    // now, enhance data structure with new element and add object
    import CompanyDataStructure2._
    val group = new Group { val name = "Working group 1" }
    val klaus = new Employee {val name ="Klaus"; var salary = 150 }
    
    dep1.subunits += group
    group.members += klaus
    
    (company -: printVis.element).accept
    (company -: salVis.element).accept
    (company -: printVis.element).accept
  }
}


object CompanyDataStructure2 {
  trait Group extends SubUnit {
    val name: String
    val members = new HashSet[Employee]
  }
}
