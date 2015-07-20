package com.thegamesquid.tivoli.eventstore

import akka.actor.{Actor, ActorLogging, Props}
import akka.io.Tcp
import com.thegamesquid.tivoli.eventstore.actor.EventMessageHandler
import com.thegamesquid.tivoli.eventstore.actor.EventMessageHandler._

class ConnectionHandler extends Actor with ActorLogging {

  import Tcp._

  def receive = {
    case Received(data) => {
      log.info("Data received! " + data)
      log.info("Date received! " + data.decodeString("UTF-8"))

      val decoded = data.decodeString("UTF-8").trim

      decoded.startsWith("<START>>") && decoded.endsWith("END") match {
        case true => {
          log.info("Sending message to EventMessageHandler")
          context.system.actorOf(Props[EventMessageHandler]) ! EventMessageRequest(decoded)
        }
        case false => {
          log.warning("Invalid message received: " + decoded)
        }
      }
    }

    case PeerClosed => {
      log.info("Peer closed!")
      context stop self
    }

    case _ => log.info("Received unknown message")
  }
}
