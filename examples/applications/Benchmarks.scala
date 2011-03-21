/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package applications

import util.PerformanceAnalysis._

object Benchmarks {
  def main(args : Array[String]) : Unit = {
    def run(runner: Runner) {
      val max = args(0).toInt
      // force initialization of vals on object..
      runner.init()

      // measure time of run
      val t_without: Float = if (max == 1) 
        measureTime {
          runner.withoutRoles()
        }
      else 
        measureTime {
          for(i <- 1 to max) runner.withoutRoles()
        }

      val t_with: Float = if (max == 1)
        measureTime {
          runner.withRoles()
        }
      else 
        measureTime {
          for(i <- 1 to max) runner.withRoles()
        }
      
      val t_withROP: Float = if (max == 1)
        measureTime {
          runner.withROP()
        }
      else 
        measureTime {
          for(i <- 1 to max) runner.withROP()
        }
      
    val slowdown_roles = t_with / t_without
    val slowdown_rop = t_withROP / t_without
    print(slowdown_roles + ":" + slowdown_rop)
    }
    
    val slowdown = args(1) match {
      case "as" => run(As)
      case "noas" => run(NoAs)
      case "callsonly" => run(CallsOnly)
      case "thesis" => run(ThesisSupervisionBenchmark)
      case _ => Console.err.print("Unknown option " + args(1))
    }
    
    // stop here for profiling
    //Console.readLine

    0
  }

}

abstract class Runner {
  def init()
  def withRoles()
  def withoutRoles()
  def withROP()
}

object As extends Runner {
  import scala.roles.Basics._
  import scala.roles._

  override def init() = {}

  override def withROP() = {}
  
  override def withRoles() {
    val a = new SimpleA {}
    val collab = new TestCollaboration {}
    a.m1
    (a -: collab.roleA).m2
    a.i = 127
    a.m1
    (a -: collab.roleA).j = 414
    (a -: collab.roleA).m2
    (a -: collab.roleA).j = 415
    (a -: collab.roleA).m2
    (a -: collab.roleA).j = 42
    (a -: collab.roleA).m2
    (a -: collab.roleA).j = 43
    (a -: collab.roleA).m2
    (a -: collab.roleA).j = 63
    (a -: collab.roleA).m2
  }
  
  override def withoutRoles() {
    val a = new A {}
    a.m1
    a.m2
    a.i = 127
    a.m1
    a.j = 414
    a.m2
    a.j = 415
    a.m2
    a.j = 42
    a.m2
    a.j = 43
    a.m2
    a.j = 63
    a.m2
  }
  
  
  trait A {
    var i = 3
    var j = 5
    def m1 = i/10+5
    def m2 = j/10+5
  }
  
  trait SimpleA {
    var i = 3
    def m1 = i/10+5
  }

  trait TestCollaboration extends TransientCollaboration {
    val roleA = new RoleA {}    
    trait RoleA extends Role[SimpleA] {
      var j = 5
      def m2 = j/10+5
    }
  }  
}

object NoAs extends Runner {
  import scala.roles.Basics._
  import scala.roles._  
  
  override def init() = {}

  override def withROP() = {}
  
  override def withRoles() {
    val a = new SimpleA {}
    val collab = new TestCollaboration {}
    a.m1
    (a -: collab.roleA).m2
    a.i = 127
    a.m1
    (a -: collab.roleA).j = 414
    (a -: collab.roleA).m2
    (a -: collab.roleA).j = 415
    (a -: collab.roleA).m2
    (a -: collab.roleA).j = 42
    (a -: collab.roleA).m2
    (a -: collab.roleA).j = 43
    (a -: collab.roleA).m2
    (a -: collab.roleA).j = 63
    (a -: collab.roleA).m2
  }
  
  override def withoutRoles() {
    val a = new A {}
    a.m1
    a.m2
    a.i = 127
    a.m1
    a.j = 414
    a.m2
    a.j = 415
    a.m2
    a.j = 42
    a.m2
    a.j = 43
    a.m2
    a.j = 63
    a.m2
  }
  
  
  trait A {
    var i = 3
    var j = 5
    def m1 = i/10+5
    def m2 = j/10+5
  }
  
  trait SimpleA {
    var i = 3
    def m1 = i/10+5
  }

  trait TestCollaboration extends TransientCollaboration {
    val roleA = new RoleA {}    
    trait RoleA extends Role[SimpleA] {
      var j = 5
      def m2 = j/10+5
    }
  }  
}

object SingleCall {
  import scala.roles.Basics._
  import scala.roles._  
 
  val c = new TestCollaboration {}
  val a = new A {}
  val simpleA = new SimpleA {}
  val proxy = (simpleA -: c.roleA)
  
  def main(args: Array[String]): Unit = {
    val max = args(0).toInt
    
    // force initialization of vals
    if (c.toString == a.toString && "abc" == simpleA.toString && proxy != "a")
      println("this never happens")
      
    val t0_with = System.nanoTime
    //var i_with = 0
    //while(i_with < max) {
    val diff_with: Float = proxy.mr - t0_with
    //  i_with = i_with + 1 
    //}
    val t_with: Float = diff_with
      
    val t0_without = System.nanoTime
    //var i_without = 0
    //while(i_without < max) {
    val diff_without: Float = a.m - t0_without
    //  i_without = i_without + 1
    //}
    val t_without: Float = diff_without
    
    println(t_with + " ns vs. " + t_without + " ns")
    print(t_with / t_without)
    // stop here for profiling
    //Console.readLine
  }
  
  trait A {
    def m = System.nanoTime
  }
  
  trait SimpleA {}

  trait TestCollaboration extends TransientCollaboration {
    val roleA = new RoleA {}    
    trait RoleA extends Role[SimpleA] {
      def mr = System.nanoTime
    }
  } 
}

object CallsOnly extends Runner {
  import scala.roles.Basics._
  import scala.roles._
 
  val simpleA = new SimpleA {}
  val collab = new TestCollaboration {}
  val a = new A {}

  override def init() = {
    if (simpleA.toString == collab.toString && "abc" == a.toString)
      println("this never happens")
  }

  override def withROP() = {}
  
  def main(args: Array[String]): Unit = {
    init()
    val t_without: Float = measureTime{
      withoutRoles()
    }
    val t_with: Float = measureTime{
      withRoles()
    }
    print(t_with / t_without)
    // stop here for profiling
    Console.readLine
  }

  def withRoles() {
    simpleA.m1
    (simpleA -: collab.roleA).m2
    simpleA.i = 127
    simpleA.m1
    (simpleA -: collab.roleA).j = 414
    (simpleA -: collab.roleA).m2
    (simpleA -: collab.roleA).j = 415
    (simpleA -: collab.roleA).m2
    (simpleA -: collab.roleA).j = 42
    (simpleA -: collab.roleA).m2
    (simpleA -: collab.roleA).j = 43
    (simpleA -: collab.roleA).m2
    (simpleA -: collab.roleA).j = 63
    (simpleA -: collab.roleA).m2
  }
  
  def withoutRoles() {
    a.m1
    a.m2
    a.i = 127
    a.m1
    a.j = 414
    a.m2
    a.j = 415
    a.m2
    a.j = 42
    a.m2
    a.j = 43
    a.m2
    a.j = 63
    a.m2
  }
  
  
  trait A {
    var i = 3
    var j = 5
    def m1 = i/10+5
    def m2 = j/10+5
  }
  
  trait SimpleA {
    var i = 3
    def m1 = i/10+5
  }

  trait TestCollaboration extends TransientCollaboration {
    val roleA = new RoleA {}    
    trait RoleA extends Role[SimpleA] {
      var j = 5
      def m2 = j/10+5
    }
  }  
}

object ThesisSupervisionBenchmark extends Runner {
  import scala.roles.Basics._
  import scala.roles._
  import collaborations.Person
  import collaborations.ThesisSupervision

  override def init() = {}

  override def withoutRoles() {
    val st1 = new Student{ val name = "Paul" }     // supervised by Uwe
    val st2 = new Student{ val name = "Klaus" }    // supervised by Uwe
    val st3 = new Student{ val name = "Tim" }      // supervised by Paul
    val st4 = new Student{ val name = "Struppi" }  // supervised by Klaus
    val st5 = new Student{ val name = "Heinz" }    // supervised by Paul
    val st6 = new Student{ val name = "Peter" }    // supervised by Uwe

    val sv1 = new Supervisor{ val name = "Paul" }  
    val sv2 = new Supervisor{ val name = "Klaus" }
    val sv3 = new Supervisor{ val name = "Uwe" }

    sv3.advise(st1)
    sv3.advise(st2)
    sv3.advise(st2)
    sv3.advise(st2)
    sv2.advise(st4)
    sv1.advise(st3)

    st1.work
    st2.work
    st3.work
    st4.work
    st5.work
    st6.work
    st3.work
    st2.work
    st6.work

    sv3.grade(st1)
    sv3.grade(st2)
    sv1.grade(st3)
    sv2.grade(st4)
    sv1.grade(st5)
    sv3.grade(st6)
}

  override def withRoles() {
    val paul = new Person{ val name = "Paul" }
    val klaus = new Person{ val name = "Klaus" }
    val tim = new Person{ val name = "Tim" }
    val struppi = new Person{ val name = "Struppi" }
    val heinz = new Person{ val name = "Heinz" }
    val peter = new Person{ val name = "Peter" }
    val uwe = new Person{ val name = "Uwe" }

    val c1 = new ThesisSupervision{}  // uwe -> paul
    val c2 = new ThesisSupervision{}  // uwe -> klaus
    val c3 = new ThesisSupervision{}  // paul -> tim
    val c4 = new ThesisSupervision{}  // klaus -> struppi
    val c5 = new ThesisSupervision{}  // paul -> heinz
    val c6 = new ThesisSupervision{}  // uwe -> peter

    (uwe -: c1.supervisor).advise
    (uwe -: c2.supervisor).advise
    (uwe -: c2.supervisor).advise
    (uwe -: c2.supervisor).advise
    (klaus -: c4.supervisor).advise
    (paul -: c3.supervisor).advise

    (paul -: c1.student).work
    (klaus -: c2.student).work
    (tim -: c3.student).work
    (struppi -: c4.student).work
    (heinz -: c5.student).work
    (peter -: c6.student).work
    (tim -: c3.student).work
    (klaus -: c2.student).work
    (peter -: c6.student).work

    (uwe -: c1.supervisor).grade
    (uwe -: c2.supervisor).grade
    (paul -: c3.supervisor).grade
    (klaus -: c4.supervisor).grade
    (paul -: c5.supervisor).grade
    (uwe -: c6.supervisor).grade

  }
  
  def withROP() = {
    import scala.collection.mutable.HashMap
    
    val ST = "Student"
    val SV = "Supervisor"
    
    trait PersonComponent {
      def hasRole(spec: String): Boolean
      def addRole(spec: String)
      def removeRole(spec: String)
      def getRole(spec: String): PersonRole
    }
    
    trait PersonCore extends PersonComponent {
      val name: String
      var title: String = ""
      
      val roles = new HashMap[String, PersonRole]()
      
      def hasRole(spec: String): Boolean = roles.contains(spec)
      def addRole(spec: String) = if (!roles.contains(spec)) roles.put(spec, createRoleFor(spec, this)) 
      def removeRole(spec: String) = roles -= spec
      def getRole(spec: String): PersonRole = roles(spec)
    }
    
    trait PersonRole extends PersonComponent {
      var core: PersonCore = _
      
      def hasRole(spec: String): Boolean = core.hasRole(spec)
      def addRole(spec: String) = core.addRole(spec)
      def removeRole(spec: String) = core.removeRole(spec)
      def getRole(spec: String): PersonRole = core.getRole(spec)
    }
    
    trait StudentRole extends PersonRole {
      var motivation = 50
      var wisdom = 0
      def work = wisdom += motivation/10
    }
    
    trait SupervisorRole extends PersonRole {
      def advise(student: StudentRole) = student.motivation += 5
      def grade(student: StudentRole) = if (student.wisdom > 80) "good" else "bad"  
    }
    
    def createRoleFor(spec: String, core2: PersonCore): PersonRole = {
      spec match {
        case ST    => val r = new StudentRole {}; r.core = core2; r 
        case SV => val r = new SupervisorRole {}; r.core = core2; r
      }
    }
    
    val paul = new PersonCore{ val name = "Paul" }
    val klaus = new PersonCore{ val name = "Klaus" }
    val tim = new PersonCore{ val name = "Tim" }
    val struppi = new PersonCore{ val name = "Struppi" }
    val heinz = new PersonCore{ val name = "Heinz" }
    val peter = new PersonCore{ val name = "Peter" }
    val uwe = new PersonCore{ val name = "Uwe" }

    uwe.addRole(SV)
    paul.addRole(SV)
    klaus.addRole(SV)
    paul.addRole(ST)
    klaus.addRole(ST)
    tim.addRole(ST)
    struppi.addRole(ST)
    heinz.addRole(ST)
    peter.addRole(ST)    
    
    if (uwe.hasRole(SV) && paul.hasRole(ST))
      uwe.getRole(SV).asInstanceOf[SupervisorRole].advise(paul.getRole(ST).asInstanceOf[StudentRole])
    else 
      throw new Exception("role not found")
    
    if (uwe.hasRole(SV) && klaus.hasRole(ST)) {
      uwe.getRole(SV).asInstanceOf[SupervisorRole].advise(klaus.getRole(ST).asInstanceOf[StudentRole])
      uwe.getRole(SV).asInstanceOf[SupervisorRole].advise(klaus.getRole(ST).asInstanceOf[StudentRole])
      uwe.getRole(SV).asInstanceOf[SupervisorRole].advise(klaus.getRole(ST).asInstanceOf[StudentRole])      
    } else 
      throw new Exception("role not found")
    
    if (klaus.hasRole(SV) && struppi.hasRole(ST))
      klaus.getRole(SV).asInstanceOf[SupervisorRole].advise(struppi.getRole(ST).asInstanceOf[StudentRole])
    else 
      throw new Exception("role not found")  
      
    if (paul.hasRole(SV) && tim.hasRole(ST))
      paul.getRole(SV).asInstanceOf[SupervisorRole].advise(tim.getRole(ST).asInstanceOf[StudentRole])
    else 
      throw new Exception("role not found")   

    if (paul.hasRole(ST))
      paul.getRole(ST).asInstanceOf[StudentRole].work
    else 
      throw new Exception("role not found")   
      
    if (klaus.hasRole(ST))
      klaus.getRole(ST).asInstanceOf[StudentRole].work
    else 
      throw new Exception("role not found")
      
    if (tim.hasRole(ST))
      tim.getRole(ST).asInstanceOf[StudentRole].work
    else 
      throw new Exception("role not found")
    
    if (struppi.hasRole(ST))
      struppi.getRole(ST).asInstanceOf[StudentRole].work
    else 
      throw new Exception("role not found")  
    
    if (heinz.hasRole(ST))
      heinz.getRole(ST).asInstanceOf[StudentRole].work
    else 
      throw new Exception("role not found")
      
    if (peter.hasRole(ST))
      peter.getRole(ST).asInstanceOf[StudentRole].work
    else 
      throw new Exception("role not found")
      
    if (tim.hasRole(ST))
      tim.getRole(ST).asInstanceOf[StudentRole].work
    else 
      throw new Exception("role not found")
    
    if (klaus.hasRole(ST))
      klaus.getRole(ST).asInstanceOf[StudentRole].work
    else 
      throw new Exception("role not found")
      
    if (peter.hasRole(ST))
      peter.getRole(ST).asInstanceOf[StudentRole].work
    else 
      throw new Exception("role not found")
      
    if (uwe.hasRole(SV) && paul.hasRole(ST))
      uwe.getRole(SV).asInstanceOf[SupervisorRole].grade(paul.getRole(ST).asInstanceOf[StudentRole])
    else 
      throw new Exception("role not found")
    
    if (uwe.hasRole(SV) && klaus.hasRole(ST))
      uwe.getRole(SV).asInstanceOf[SupervisorRole].grade(klaus.getRole(ST).asInstanceOf[StudentRole])
    else 
      throw new Exception("role not found")
      
    if (paul.hasRole(SV) && tim.hasRole(ST))
      paul.getRole(SV).asInstanceOf[SupervisorRole].grade(tim.getRole(ST).asInstanceOf[StudentRole])
    else 
      throw new Exception("role not found")
      
    if (klaus.hasRole(SV) && struppi.hasRole(ST))
      klaus.getRole(SV).asInstanceOf[SupervisorRole].grade(struppi.getRole(ST).asInstanceOf[StudentRole])
    else 
      throw new Exception("role not found")
      
    if (paul.hasRole(SV) && heinz.hasRole(ST))
      paul.getRole(SV).asInstanceOf[SupervisorRole].grade(heinz.getRole(ST).asInstanceOf[StudentRole])
    else 
      throw new Exception("role not found")
      
    if (uwe.hasRole(SV) && peter.hasRole(ST))
      uwe.getRole(SV).asInstanceOf[SupervisorRole].grade(peter.getRole(ST).asInstanceOf[StudentRole])
    else 
      throw new Exception("role not found")
  }

  trait Supervisor extends Person {
    def advise(student: Student) = student.motivation += 5
    def grade(student: Student) = if (student.wisdom > 80) "good" else "bad"
  }
    
  trait Student extends Person {
    var motivation = 50
    var wisdom = 0
    def work = wisdom += motivation/10
  }
}
