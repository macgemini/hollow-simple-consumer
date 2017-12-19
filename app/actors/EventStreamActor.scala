package actors

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import commons.{LookupBusImpl, MsgEnvelope}

/**
  * Created by mac on 01.03.17.
  */
class EventStreamActor(out: ActorRef, eventBus: LookupBusImpl) extends Actor with ActorLogging {

  val subscription = eventBus.subscribe(self,"events")

  override def receive: Receive = {
    case msg: String => {
      val message = s"Message: ${msg}"
      out ! message
    }
  }
}

object EventStreamActor {
  def props(out: ActorRef, eventBus: LookupBusImpl) = Props(new EventStreamActor(out,eventBus))
}
