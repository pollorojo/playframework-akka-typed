package controllers

import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.{ActorRef, ActorSystem, Scheduler}
import akka.util.Timeout
import domain.NumeratorActor
import play.api.libs.json.{Json, OWrites}

import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationInt

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents,
                               actorSystem: ActorSystem[_]) // Binding defined in modules.Module class (ScalaGuice)
                              (implicit val ec: ExecutionContextExecutor,
                               implicit val sc: Scheduler) extends BaseController {

  implicit val tt: Timeout = Timeout(30.seconds) // <- Timeout implicit definition for async operations

  var numerator: ActorRef[NumeratorActor.Command] = actorSystem.systemActorOf(NumeratorActor(), "Numerator")

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  implicit val num: OWrites[NumeratorActor.OrderNumberResponse] = Json.writes[NumeratorActor.OrderNumberResponse]

  def numerate(): Action[AnyContent] = Action.async {
    /*
    To do this operation, we need these implicits:
      - Timeout: implicit val tt: Timeout = Timeout(30.seconds)
      - ExecutionContext: implicit val ec: ExecutionContextExecutor
      - Scheduler: implicit val sc: Scheduler
     */
    numerator.ask[NumeratorActor.OrderNumberResponse](replyTo => NumeratorActor.GetNextNumber(replyTo))
      .map(response => Ok(Json.toJson(response)))
  }
}
