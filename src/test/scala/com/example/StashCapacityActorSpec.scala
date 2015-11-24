package com.example

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
 
class StashCapacityActorSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {
 
  def this() = this(ActorSystem("MySpec"))
 
  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "Bounded actor" must {
    "gracefully drop messages" in {
      val stashActor:TestActorRef[StashCapacityActor] =
        TestActorRef(StashCapacityActor.props)

      (1 to 501).foreach { i =>
        stashActor ! "msg" + i
        //(??)I thought the test actor ref should default to calling thread dispatcher?
        Thread.sleep(10) //sleep to maintain ordering for the assertions
      }

      stashActor ! "open"

      Thread.sleep(10)

      val msgs = stashActor.underlyingActor.msgs

      msgs.size should equal(50)
      msgs.head should equal("msg1")
      msgs.last should equal("msg50")

    }
  }
}
