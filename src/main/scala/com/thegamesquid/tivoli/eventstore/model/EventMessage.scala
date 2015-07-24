package com.thegamesquid.tivoli.eventstore.model

import java.text.SimpleDateFormat
import java.util.Date
import java.sql.Timestamp
import scala.util.{ Failure, Success, Try }

case class EventMessage (
  hostname: String,
  agentType: String,
  situationName: String,
  situationStatus: String,
  integrationType: String,
  timeStamp: Timestamp,
  properties: Map[String, String],
  details: Map[String, String]
)

object EventMessage {
  def apply(eifString: String) = {
    val eifMessage = parseMessage(eifString)
    val eifDetails = parseMessageDetails(eifMessage("SITUATION_EVENTDATA"))
    new EventMessage(
      getHostname(eifMessage("SITUATION_ORIGIN")),
      getAgentType(eifMessage("SITUATION_ORIGIN")),
      eifMessage("SITUATION_NAME"),
      eifMessage("SITUATION_STATUS"),
      eifMessage("INTEGRATION_TYPE"),
      getTimestamp(eifMessage("SITUATION_TIME")).orNull, // TODO: Clean this up
      eifMessage.filter(t => t._1 != "SITUATION_EVENTDATA"),
      eifDetails
    )
  }

  private def parseMessage(eifString: String) = {
    val messagePattern = "([A-z0-9]+?='(.*?'))".r
    messagePattern.findAllIn(eifString)                                     // Find all matching patterns as defined by messagePattern
              .toList                                                       // Change the resultset to a List
              .map(_.split("=", 2))                                         // Map the List to an Array of Arrays by splitting on '='
              .map(a => (a(0).toUpperCase, a(1).replace("'", ""))).toMap    // Map to an Array of Tuple2s
  }

  private def parseMessageDetails(eifDetailString: String) = {
    val messagePattern = "([^;]+?=([^;]*)+)".r
    messagePattern.findAllIn(eifDetailString)                               // Find all matching patterns as defined by messagePattern
              .toList                                                       // Change the resultset to a List
              .map(_.split("=", 2))                                         // Map the List to an Array of Arrays by splitting on '='
              .map(a => (a(0).toUpperCase, a(1).replace("'", ""))).toMap    // Map to an Array of Tuple2s
  }

  private def getTimestamp(s: String): Option[Timestamp] = {
    s match {
      case "" => None
      case _ => {
        val format = new SimpleDateFormat("MM/dd/yyyy' 'HH:mm:ss.SSS")
        Try(new Timestamp(format.parse(s).getTime)) match {
          case Success(t) => Some(t)
          case Failure(_) => None
        }
      }
    }
  }

  private def getHostname(s: String): String = {
    val hostPattern = """(?:Primary:)?([A-z0-9]*):(?:[A-z0-9])*""".r
    val Some(reg) = hostPattern.findFirstMatchIn(s)
    reg.group(1)
  }

  private def getAgentType(s: String): String = {
    val agentPattern = """(?:Primary:)?(?:[A-z0-9]*):([A-z0-9]*)""".r
    val Some(reg) = agentPattern.findFirstMatchIn(s)
    reg.group(1)
  }
}