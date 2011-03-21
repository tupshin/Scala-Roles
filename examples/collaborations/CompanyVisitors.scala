/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package collaborations;

import scala.roles.dp._
import applications.CompanyDataStructure._

object CompanyVisitors {

  trait SalaryVisitor extends Visitor2 {
    override def traverse(el: AnyRef) = el match {
      case c: Company      => c.departments.toList
      case d: Department   => d.manager :: d.subunits.toList
      case _               => Nil
    }
    
    override def createVisitor: AnyRef = 
      new {
        def visitEmployee(e: Employee) = { e.salary += 20 }
      }
  }
  
  trait PrintVisitor extends Visitor2 {
    override def traverse(el: AnyRef): Seq[AnyRef] = Nil
    
    override def createVisitor: AnyRef =
      new {
        def visitCompany(c: Company) = {
          println("\nCompany " + c.name + " with departments:")
          c.departments.foreach(d => (d -: element).accept)
        }
        
        def visitDepartment(d: Department) = {
          println("Department " + d.name + " with Manager:")
          (d.manager -: element).accept
          println("..and subunits:")
          d.subunits.foreach(s => (s -: element).accept) 
        }
        
        def visitEmployee(e: Employee) = println("Employee " + e.name + ", salary: " + e.salary)
    }
  }
}
