package com.thegamesquid.tivoli.eventstore

import java.net.InetSocketAddress
import akka.actor.{Actor, ActorLogging, Props}
import akka.io.{IO, Tcp}

class Server extends Actor with ActorLogging {

  import Tcp._
  import context.system

  // To create a TCP server and listen for inbound connections, a Bind command has to be sent to the TCP manager
  // This will instruct the TCP manager to listen for TCP connections on a particular InetSocketAddress
  IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 5000))

  def receive = {
    // Receive message from IO(Tcp) on succesful bind
    case Bound(localAddress) => log.info("Bound! " + localAddress)

    // Command failed
    case CommandFailed(_: Bind) => context stop self

    // Client has connected
    case Connected(remote, local) => {
      log.info(remote + " connected to " + local)

      // Create an actor instance to serve as the connection handler for the client
      val handler = context.actorOf(Props[ConnectionHandler])
      val connection = sender()

      // Register the handler
      connection ! Register(handler)
    }
  }
}