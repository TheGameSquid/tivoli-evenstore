package com.thegamesquid.tivoli.eventstore.actor

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }
import akka.actor.{Actor, ActorLogging, Props }
import com.thegamesquid.tivoli.eventstore.model.EventMessage

class EventMessageHandler extends Actor with ActorLogging {
  import EventMessageHandler._

  def receive = {
    case m @ EventMessage(hostname, agentType, situationName, situationStatus, integrationType, timeStamp, properties, details) => {
      log.info(m.properties.toString)
    }

    case EventMessageRequest(eventString) => {
      val e @ message = EventMessage(eventString)
      log.info(s"Processed message for ${message.hostname}")

      // Check if the ManagedSystem actor already exists
      implicit val timeout: akka.util.Timeout = 5000
      log.info(s"akka://server/user/${message.hostname}")
      context.system.actorSelection(s"akka://server/user/${message.hostname}").resolveOne().onComplete {
        // The ManagedSystem actor already exists, send him an EventMessage
        case Success(actor) => {
          log.info(s"${message.hostname} actor already exists, passing message")
          actor ! ManagedSystem.ManagedSystemEvent(e)
        }
        // The ManagedSystem actor does not exist, create it and pass the message
        case Failure(ex) => {
          log.info(s"${message.hostname} actor doesn't exist, create it and pass the message")
          context.system.actorOf(Props(new ManagedSystem(message.hostname, message.agentType)), message.hostname) ! ManagedSystem.ManagedSystemEvent(e)
        }
      }
    }

    case _ => log.warning("Wut")
  }
}

object EventMessageHandler {
  sealed trait EventMessageHandlerRequest
  case class EventMessageRequest(eventString: String) extends EventMessageHandlerRequest
}