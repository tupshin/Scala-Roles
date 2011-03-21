/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

/*
// Problem: To replace state pattern with roles, objects must be able to change access and manipulate their own roles.

object StateTestApp {

  def main(args: Array[String]) = {
    
    var myConn = new TCPConnection  // TCPConnection with TCPClosed

    myConn = tcpConn.open   // TCPConnection with TCPOpen

    myConn.receive
    
    myConn = myConn.close

    myConn.receive // type error



    
  }

  trait TCPClosed {
    def open = {
      this.core -: tcp.open
    }
  }

  trait TCPOpen {
    def close = {
      this.core -: tcp.close
    }

    def receive = { }
  }

  
}
*/
