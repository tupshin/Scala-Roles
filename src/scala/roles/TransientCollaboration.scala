/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package scala.roles

import scala.collection.mutable.{ArrayBuffer}
import java.util.HashMap
import scala.roles.internal._

trait TransientCollaboration extends Collaboration {
   
  trait Role[Player <: AnyRef] extends TransientRole[Player]

  trait RoleMapper[Player <: AnyRef, RoleType <: AbstractRole[Player]] extends AbstractRoleMapper[Player, RoleType]
  
}

