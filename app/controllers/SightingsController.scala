package controllers

import javax.inject._

import model.{ Sighting, Specie, SightingInterface, SpecieInterface }
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.functional.syntax._
import org.joda.time.DateTime
import scala.concurrent.ExecutionContext
import java.text.SimpleDateFormat
import scala.util.Try
import scala.concurrent.Future

@Singleton
class SightingsController @Inject() (sighting: SightingInterface, specie: SpecieInterface, cc: ControllerComponents)
                                    (implicit executionContext: ExecutionContext) extends AbstractController(cc) {


  implicit val readDateTime: Reads[DateTime] = new Reads[DateTime] {

    override def reads(json: JsValue): JsResult[DateTime] = json match {
      case JsString(d) => Try(new DateTime(d)).map(t => JsSuccess(t)).getOrElse(error(json))
      case _           => error(json)
    }

    private def error(json: JsValue) = JsError(s"Unable to parse $json into a DateTime with format EEE, dd MMM yyyy HH:mm:ss z ")
  }

  implicit val sightingReads: Reads[Sighting] = (
    (JsPath \ "description").read[String] and
    (JsPath \ "species").read[String] and
    (JsPath \ "dateTime").read[DateTime] and
    (JsPath \ "count").read[Int] and
    (JsPath \ "id").read[Long])(Sighting.apply _)

  implicit val ec = ExecutionContext.global

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def sightings() = Action.async { request =>
    val ducks = sighting.listAll
    ducks.map(seqSightings => seqSightings.map(_.toString()).mkString("\n\n"))
      .map(x => Ok(x.toString()))
  }

  def reportSightings() = Action(parse.json).async { request =>
    val ducksResult: JsResult[Sighting] = request.body.validate[Sighting]
    ducksResult.fold(
      errors => {
        Future(
          BadRequest(Json.obj("status" -> "Bad Request", "message" -> errors.toString())))
      },
      ducks => {
        sighting.add(ducks)
          .map(result => Ok(Json.obj("status" -> "Ok", "message" -> result)))
          .recover {
            case e: Exception =>
              InternalServerError(
                Json.obj("status" -> "Internal Service error", "message" -> e.toString()))
          }
      })
  }

  def species() = Action.async { request =>
    val duckSpecies = specie.listSpecies
    duckSpecies.map(ds => ds.map(_.toString()).mkString(", "))
      .map(x => Ok(x))
  }

}



















