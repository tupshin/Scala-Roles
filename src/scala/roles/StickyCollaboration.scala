/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package scala.roles

import scala.roles.internal._

trait StickyCollaboration extends Collaboration {

  trait Role[Player <: AnyRef] extends StickyRole[Player]

}

