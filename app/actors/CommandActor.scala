package actors

import actors.commands.events.AttachEvents
import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorRef, Props}

/**
  * Created by mac on 24.02.17.
  */
class CommandActor(out: ActorRef, orchestrationActor: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case s: String =>  orchestrationActor forward s
    case o: OutputMessage => {
      out ! o.meassage
    }
    case aj: AttachEvents => out ! aj.toString()
  }
}


object CommandActor {
  def props(out: ActorRef, orchestrationActor: ActorRef) = Props(new CommandActor(out,orchestrationActor))
}