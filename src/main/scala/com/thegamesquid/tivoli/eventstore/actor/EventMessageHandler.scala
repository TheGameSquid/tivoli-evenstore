package com.thegamesquid.tivoli.eventstore.actor

import akka.actor.{Actor, ActorLogging}
import com.thegamesquid.tivoli.eventstore.model.EventMessage

class EventMessageHandler extends Actor with ActorLogging {
  import EventMessageHandler._

  def receive = {
    case m @ EventMessage(hostname, situationName, situationStatus, integrationType, timeStamp, properties, details) => {
      log.info(m.properties.toString)
    }

    case EventMessageRequest(eventString) => {
      val message = EventMessage(eventString)
      log.info(s"Processed message for ${message.hostname}")
    }

    case _ => log.warning("Wut")
  }
}

object EventMessageHandler {
  sealed trait EventMessageHandlerRequest
  case class EventMessageRequest(eventString: String) extends EventMessageHandlerRequest
}