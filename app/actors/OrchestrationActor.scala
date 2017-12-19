package actors

import javax.inject.Inject

import actors.commands.{BaseCommand, UnknownCommand}
import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, Props}
import actors.commands._
import actors.commands.events._
import actors.commands.scenarios._
import actors.commands.events._
import akka.event.japi.LookupEventBus
import commons.LookupBusImpl
import services._
import play.api.libs.json._
import scala.util.{Failure, Success, Try}

/**
  * Created by mac on 24.02.17.
  */
class OrchestrationActor @Inject() (eventBus: LookupBusImpl) extends Actor with ActorLogging {

  val helpActor = context.actorOf(Props[HelpActor],"HelpActor")
  val eventsActor = context.actorOf(Props(new EventsActor(eventBus)), "EventsActor")

  override def receive: Receive = {
    case s: String =>
      parseCommand(s) match {
        case command: EventsCommand => eventsActor forward command
        case command: GetHelp => helpActor forward command
        case command: UnknownCommand => sender() ! OutputMessage(command.message)
      }
    case _ => sender() ! "Dunno what is this"
  }

  def parseCommand(input: String): BaseCommand = {
    val cleansed = input.split(" ").map(x => x.trim)
    val commandHead = cleansed.headOption
    val params = cleansed.drop(1).toList
    val commandString = commandHead.getOrElse(UnknownCommand("No command was specified - use getHelp to print all commands"))

    val command: BaseCommand = commandString match {
      case "getHelp" => new GetHelp
      case "runEvents" => params match {
        case Nil => new RunEvents
        case _ => UnknownCommand(s"Wrong list of params for a command: $commandString")
      }
      case "killEvents" => params match {
        case Nil => new KillEvents
        case _ => UnknownCommand(s"Wrong list of params for a command: $commandString")
      }
      case x => UnknownCommand(s"Command ${x} was not found, use getHelp to print all commands")
    }
    command
  }

}
