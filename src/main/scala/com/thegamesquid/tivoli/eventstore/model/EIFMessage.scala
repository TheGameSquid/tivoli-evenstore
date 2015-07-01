package com.thegamesquid.tivoli.eventstore.model

case class EIFMessage (properties: Map[String, String], details: Map[String, String])

object EIFMessage {
  def apply(eifString: String) = {
    val eifMessage = ParseMessage(eifString)
    new EIFMessage(eifMessage.filter(t => t._1 != "SITUATION_EVENTDATA"), ParseMessageDetails(eifMessage("SITUATION_EVENTDATA")))
  }

  def ParseMessage(eifString: String) = {
    val messagePattern = "([A-z0-9]+?='(.*?'))".r
    messagePattern.findAllIn(eifString)                                     // Find all matching patterns as defined by messagePattern
              .toList                                                       // Change the resultset to a List
              .map(_.split("=", 2))                                         // Map the List to an Array of Arrays by splitting on '='
              .map(a => (a(0).toUpperCase, a(1).replace("'", ""))).toMap    // Map to an Array of Tuple2s
  }

  def ParseMessageDetails(eifDetailString: String) = {
    val messagePattern = "([^;]+?=([^;]*)+)".r
    messagePattern.findAllIn(eifDetailString)                               // Find all matching patterns as defined by messagePattern
              .toList                                                       // Change the resultset to a List
              .map(_.split("=", 2))                                         // Map the List to an Array of Arrays by splitting on '='
              .map(a => (a(0).toUpperCase, a(1).replace("'", ""))).toMap    // Map to an Array of Tuple2s
  }
}