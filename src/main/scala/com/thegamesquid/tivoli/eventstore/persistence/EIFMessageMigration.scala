package com.thegamesquid.tivoli.eventstore.persistence

import Context._

object EIFMessageMigration {

}

class Person(var name: String) extends Entity

class CreateEIFMessageTableMigration extends Migration {
  def timestamp = System.currentTimeMillis / 1000
  def up = {
    table[Person]
      .createTable(
        _.column[String]("name"))
  }
}
