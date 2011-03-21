#!/bin/sh

scala -cp bin/ applications.Benchmarks 10 as
scala -cp bin/ applications.Benchmarks 10 noas
scala -cp bin/ applications.Benchmarks 10 thesis
scala -cp bin/ applications.Benchmarks 10 callsonly
#scala -cp bin/ applications.ChainOfRespTestApp
scala -cp bin/ applications.CompositeTestApp
scala -cp bin/ applications.CompoundTypes
scala -cp bin/ applications.Decorator
scala -cp bin/ applications.EasierVisitor
scala -cp bin/ applications.Equality
scala -cp bin/ applications.FlexibleTemplateMethod
scala -cp bin/ applications.MediatorTestAppRoles
scala -cp bin/ applications.MediatorTestAppNoRoles
scala -cp bin/ applications.ObserverTestApp
#scala -cp bin/ applications.RefinementTypes
scala -cp bin/ applications.SelfProblem
scala -cp bin/ applications.SetterGetter
scala -cp bin/ applications.StickyThesisSupervisionTestApp
scala -cp bin/ applications.ThesisSupervisionTestApp2
scala -cp bin/ applications.ThesisSupervisionTestApp
#scala -cp bin/ applications.Visitor2TestApp
#scala -cp bin/ applications.VisitorTestApp
