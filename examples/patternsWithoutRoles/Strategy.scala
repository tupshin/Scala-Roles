/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package patternsWithoutRoles

object Strategy {
  
  trait Rectangle {
    val p1: (Int,Int)
    val p2: (Int,Int)
    
    def paint = {
      drawLine(p1, (p1._1, p2._2)) 
      drawLine((p1._1, p2._2), p2)
      drawLine(p2, (p2._1,p1._2))
      drawLine((p2._1,p1._2), p1)
    }

    var drawLine: ((Int,Int), (Int,Int)) => Unit = _
  }

  def dottedLine(start: (Int,Int), end: (Int,Int)): Unit = println(start + " ... " + end)
  
  def dashedLine(start: (Int,Int), end: (Int,Int)): Unit = println(start + " --- " + end)

  def main(args: Array[String]) = {
    val rec = new Rectangle{ val p1=(3,3); val p2=(5,8) }
    rec.drawLine = dottedLine
    rec.paint
    rec.drawLine = dashedLine
    rec.paint
  } 
  
}
