/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package applications;

import scala.roles.dp.Composite
import scala.roles.Basics._

import collaborations.ThesisSupervision

object CompositeTestApp {
  
  def main(args : Array[String]) : Unit = {
    val f1 = new Figure {}
    val f2 = new Figure {}
    val f3 = new Figure {}
    val f4 = new SpecialFigure {}

    val c = new Composite[Figure]{}
  //  implicit def figure2parent(f: Figure) = f -: c.parent
  //  implicit def figure2child(f: Figure) = f -: c.child
 
    (f1 -: c.parent).addChild(f2 -: c.child)
    (f1 -: c.parent).addChild(f3 -: c.child)
    (f3 -: c.parent).addChild(f4 -: c.child)
    // f1.addChild(f4) causes exception
    f1.text = "abc"
    
    println((f1 -: c.parent).getChild(0))
    println((f1 -: c.parent).getChild(1))
  
    println((f4 -: c.child).getParent)
    
    f4.text = "I'm a leaf"
    
    val f5 = new Figure{}
    c.child.transfer(f4,f5)
    println(f5 -: c.child)
    println((f5 -: c.child).getParent)
    
    c.child.transfer(f5,f4)
    
    (f3 -: c.parent).removeChild(f4 -: c.child)
    
    println(f1.text)
    println(f4.text)

    println(f4.x)
    println((f4 -: c.child).x)

    
    // f3.getChild(0) causes exception 


    // test roles of role
    val c2 = new Composite[Figure]{}
    ((f1 -: c.parent) -: c2.parent).addChild((f2 -: c.parent) -: c2.child)
    val roleInRole = ((f1 -: c.parent) -: c2.parent)

    println(roleInRole.getChild(0))
  }
  
}

trait Figure {
  var text = ""
}

trait SpecialFigure extends Figure {
  var x = 23
}
