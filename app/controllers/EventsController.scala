package controllers

import javax.inject.{Inject, Named}

import actors.{CommandActor, EventStreamActor, OrchestrationActor}
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.Materializer
import commons.LookupBusImpl
import play.api.mvc._
import play.api.libs.streams._

class EventsController @Inject()(implicit system: ActorSystem, materializer: Materializer, @Named("OrchestrationActor") orchestrationActor: ActorRef, eventBus: LookupBusImpl) extends Controller {

  def index = Action {
    Ok(views.html.index("Events Controller"))
  }

  def command = WebSocket.accept[String,String] { request =>
    ActorFlow.actorRef(out => CommandActor.props(out,orchestrationActor))
  }

  def stream = WebSocket.accept[String,String] { request =>
    ActorFlow.actorRef(out => EventStreamActor.props(out,eventBus))
  }
}
