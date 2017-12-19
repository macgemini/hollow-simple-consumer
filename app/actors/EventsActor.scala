package actors

import actors.commands.events._
import akka.actor._
import commons.{LookupBusImpl, MsgEnvelope}
import configs.Config
import scala.concurrent.duration._

/**
  * Created by mac on 24.02.17.
  */
class EventsActor(eventBus: LookupBusImpl) extends Actor with ActorLogging {

  val config = Config()
  implicit val timeout = akka.util.Timeout(2 seconds)

  override def receive:  Receive = {
    case jb: RunEvents => {
      sender() ! new AttachEvents
    }
    case jb: KillEvents => {

    }
    case _ => sender() ! OutputMessage("Jobs: Unknown command or parameters list")
  }

}
