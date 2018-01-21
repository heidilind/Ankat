package controllers

import javax.inject._

import model.{ Sighting, SightingStorage }
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.functional.syntax._

import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class SightingsController @Inject() (sightingStorage: SightingStorage, cc: ControllerComponents)(implicit executionContext: ExecutionContext) extends AbstractController(cc) {

  implicit val sightingWrites = new Writes[Sighting] {
    def writes(sighting: Sighting) = Json.obj(
      "description" -> sighting.description,
      "count" -> sighting.count)
  }

  implicit val sightingReads: Reads[Sighting] = (
    (JsPath \ "description").read[String] and
    (JsPath \ "count").read[Int] and
    (JsPath \ "id").read[Long]
    )(Sighting.apply _)

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  implicit val ec = ExecutionContext.global

  def sightings() = Action.async { request =>
    val ducks = sightingStorage.listAll
    ducks.map(seqSightings => seqSightings.map(_.toString()).mkString(","))
      .map(x => Ok(x))
  }

  def reportSightings() = Action(parse.json) { request =>
    val ducksResult: JsResult[Sighting] = request.body.validate[Sighting]
    ducksResult.fold(
      errors => {
        BadRequest(Json.obj("message" -> errors.toString()))
      },
      ducks => {
        sightingStorage.add(ducks)
        Ok(Json.obj("status" -> "Ok", "message" -> "Sighting added!"))
      })
  }

}



















