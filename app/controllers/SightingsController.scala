package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import play.api.libs.json.JsValue
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class SightingsController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def sightings() = Action {
    Ok(sights)
  }

  def reportSightings() = Action { request =>
    request.body.asJson match {

      case Some(json) =>
        sights = sights ++ Json.arr(json)
        Ok(Json.obj(
          "message" -> "The request contained JSON data",
          "data" -> json))

      case None =>
        Ok(Json.obj(
          "message" -> "The request contained no JSON data"))
    }
  }

  def sight(id: String, specie: String, description: String, dateTime: String, count: Int) = {
    Json.obj(
      "id" -> id,
      "species" -> specie,
      "description" -> description,
      "dateTime" -> dateTime,
      "count" -> count)
  }

  var sights = Json.arr(
    sight("1", "gadwall", "All yout ducks are belong to us", "2016-10-01T01:01:00Z", 1),
    sight("2", "lesser scaup", "This is awesome", "2016-12-13T12:05:00Z", 5),
    sight("3", "canvasback", "...", "2016-12-13T12:05:00Z", 5),
    sight("4", "mallard", "Getting tired", "2016-12-13T12:05:00Z", 5),
    sight("5", "redhead", " think this one ie called Alfred J.", "2016-12-13T12:05:00Z", 5),
    sight("6", "redhead",
      "If it looks like a duck, swims like a duck, and quacks like a duck, then it probably is a duck.",
      "2016-12-13T12:05:00Z", 5),
    sight("7", "mallard", "Too many ducks to be counted", "2016-12-13T12:05:00Z", 5),
    sight("8", "canvasback", "KWAAK!!!1", "2016-12-13T12:05:00Z", 5))

}
