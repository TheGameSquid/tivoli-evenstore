package com.thegamesquid.tivoli.eventstore.actor

import akka.actor.{Actor, ActorLogging}
import net.fwbrasil.activate.ActivateContext
import net.fwbrasil.activate.storage.StorageFactory

import com.thegamesquid.tivoli.eventstore.model.EIFMessage

class EIFMessageHandler extends Actor with ActorLogging {

def receive = {
    case m @ EIFMessage(properties, details) => {
      println(m.properties)
    }

    case _ => println("Wut")
  }
}
