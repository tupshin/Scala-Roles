/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package patternsWithoutRoles

object CommandExample {
  abstract class Command {
    def execute
  }

  class OpenCommand(val app: Application) extends Command {
    def execute = {
      val filename = askUser
      if (filename != "") {
        val doc = new Document
        app.add(doc)
        doc.open
      }      
    }

    protected def askUser = "filename.txt"
  }

  class PasteCommand(val doc: Document) extends Command {
    def execute = 
      doc.paste
  }

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

  class MenuItem(val cmd: Command, val name: String) {
    def enable {}
    def disable {}
    def onClick = 
      cmd.execute
  }

  def main(args: Array[String]) = {
    val app = new Application
    val doc = new Document

    val openItem = new MenuItem(new OpenCommand(app), "open") 
    val pasteItem = new MenuItem(new PasteCommand(doc), "paste")

    openItem.onClick
    pasteItem.onClick
  }

}
