package com.thegamesquid.tivoli.eventstore.actor

import akka.actor.{ Actor, ActorLogging }
import com.thegamesquid.tivoli.eventstore.model.EventMessage

class ManagedSystem (hostname: String, agentType: String) extends Actor with ActorLogging {
  import ManagedSystem._

  val properties: Map[String, String] = Map.empty

  def receive = {
    case ManagedSystemEvent(message) => {
      log.info(message.situationName)
      log.info(message.situationStatus)
    }
  }
}

object ManagedSystem {
  sealed trait ManagedSystemRequest
  case class ManagedSystemEvent(eventMessage: EventMessage) extends ManagedSystemRequest
}
