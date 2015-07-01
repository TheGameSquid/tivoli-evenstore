package com.thegamesquid.tivoli.eventstore

import akka.actor.{ ActorSystem, Props }

object TivoliEventStore extends App {
  val system = ActorSystem("server")
  val server = system.actorOf(Props(new Server), "server")
}
