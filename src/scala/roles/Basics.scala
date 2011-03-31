/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package scala.roles;

import internal.Collaboration
import internal.HasCore

object Basics {

  implicit def anyRef2HasAs[P <: AnyRef](o: P) = new HasAs[P] { val core = o }

  trait HasAs[P <: AnyRef] {
    val core: P
    
    def as(role: TransientCollaboration#Role[P]): core.type with TransientCollaboration#Role[P] = 
      (core -: role).asInstanceOf[core.type with role.type]

    def as[R <: TransientCollaboration#AbstractRole[P]](rolemapper: TransientCollaboration#RoleMapper[P, R]): R with core.type =
      (core -: rolemapper).asInstanceOf[R with core.type]
  }
}

