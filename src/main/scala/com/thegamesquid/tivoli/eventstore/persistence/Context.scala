package com.thegamesquid.tivoli.eventstore.persistence

import net.fwbrasil.activate.ActivateContext
import net.fwbrasil.activate.storage.memory.TransientMemoryStorage

object Context extends ActivateContext {
  val storage = new TransientMemoryStorage
}
