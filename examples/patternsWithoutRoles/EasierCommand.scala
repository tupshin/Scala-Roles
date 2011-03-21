/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package patternsWithoutRoles

// the Command pattern can be replaced by closures (only if commands are simply executed, but not if an undo mechanism is required)
object EasierCommand {
  class Application {
    def add(d: Document) {}
    def configure {}
    def restart {}
  }

  class Document {
    def length {}
    def open {}
    def paste {}
  }

  class MenuItem(cmd: => Unit, val name: String) {
    def enable {}
    def disable {}
    def onClick = cmd
  }

  val app = new Application
  val doc = new Document

  def main(args: Array[String]) = {

    // a function replacing the OpenCommand
    def openCmd(app: Application) = {
      def askUser = "filename.txt"
      val filename = askUser
      if (filename != "") {
        val doc = new Document
        app.add(doc)
        doc.open
      }
    }

    // a function replacing the PasteCommand
    def pasteCmd(doc: Document) = doc.paste

    val openItem = new MenuItem(openCmd(app), "open") 
    val pasteItem = new MenuItem(pasteCmd(doc), "paste")

    openItem.onClick
    pasteItem.onClick
  }

}
