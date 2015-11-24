package com.example

import akka.actor.{Actor, Props, Stash}

class StashCapacityActor extends Actor with Stash {
  var msgs = List.empty[String]

  def receive = {
    case "open" =>
      context.become(open)
      unstashAll()
    case msg: String =>
      stash()
  }

  def open: Receive = {
    case msg: String =>
      msgs = msgs :+ msg
  }
}

object StashCapacityActor {
  def props = Props(classOf[StashCapacityActor]).withMailbox("stash-capacity-mailbox")
}