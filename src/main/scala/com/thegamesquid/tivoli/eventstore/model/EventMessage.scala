package com.thegamesquid.tivoli.eventstore.model

import java.text.SimpleDateFormat
import java.util.Date
import java.sql.Timestamp
import scala.util.{ Failure, Success, Try }

case class EventMessage (
  hostname: String,
  situationName: String,
  situationStatus: String,
  integrationType: String,
  timeStamp: Timestamp,
  properties: Map[String, String],
  details: Map[String, String]
)

object EventMessage {
  def apply(eifString: String) = {
    val eifMessage = ParseMessage(eifString)
    val eifDetails = ParseMessageDetails(eifMessage("SITUATION_EVENTDATA"))
    val hostname = eifMessage("HOSTNAME")
    val situationName = eifMessage("SITUATION_NAME")
    val situationStatus = eifMessage("SITUATION_STATUS")
    val integrationType = eifMessage("INTEGRATION_TYPE")
    val timeStamp = getTimestamp(eifMessage("SITUATION_TIME"))
    new EventMessage(
      hostname,
      situationName,
      situationStatus,
      integrationType,
      timeStamp.orNull, // TODO: Clean this up
      eifMessage.filter(t => t._1 != "SITUATION_EVENTDATA"),
      eifDetails
    )
  }

  private def ParseMessage(eifString: String) = {
    val messagePattern = "([A-z0-9]+?='(.*?'))".r
    messagePattern.findAllIn(eifString)                                     // Find all matching patterns as defined by messagePattern
              .toList                                                       // Change the resultset to a List
              .map(_.split("=", 2))                                         // Map the List to an Array of Arrays by splitting on '='
              .map(a => (a(0).toUpperCase, a(1).replace("'", ""))).toMap    // Map to an Array of Tuple2s
  }

  private def ParseMessageDetails(eifDetailString: String) = {
    val messagePattern = "([^;]+?=([^;]*)+)".r
    messagePattern.findAllIn(eifDetailString)                               // Find all matching patterns as defined by messagePattern
              .toList                                                       // Change the resultset to a List
              .map(_.split("=", 2))                                         // Map the List to an Array of Arrays by splitting on '='
              .map(a => (a(0).toUpperCase, a(1).replace("'", ""))).toMap    // Map to an Array of Tuple2s
  }

  private def getTimestamp(s: String) : Option[Timestamp] = {
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
}