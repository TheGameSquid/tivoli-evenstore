package com.thegamesquid.tivoli.eventstore

import akka.actor.{Actor, ActorLogging}
import akka.io.Tcp
import com.thegamesquid.tivoli.eventstore.model.{ EIFMessage }

class ConnectionHandler extends Actor with ActorLogging {

  import Tcp._

  def receive = {
    case Received(data) => {
      log.info("Data received! " + data)
      log.info("Date received! " + data.decodeString("UTF-8"))

      val decoded = data.decodeString("UTF-8").trim

      val message: Option[EIFMessage] = decoded.startsWith("<START>>") && decoded.endsWith("END") match {
        case true => Some(EIFMessage(decoded))
        case false => {
          log.warning("Invalid message received: " + decoded)
          None
        }
      }

      val messageG = message.getOrElse()
    }

    case PeerClosed => {
      log.info("Peer closed!")
      context stop self
    }

    case _ => log.info("Received unknown message")
  }
}
