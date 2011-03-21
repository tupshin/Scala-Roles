/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package util;

object PerformanceAnalysis {
  def measureTime(body: => Unit): Long = {
    val t0 = System.nanoTime
    body
    System.nanoTime - t0
  }
}
