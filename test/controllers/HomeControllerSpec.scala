package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json
import play.mvc._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "SightingsController GET" should {

    "render the index page from a new instance of controller" in {
      val controller = inject[SightingsController]
      val home = controller.index().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Welcome to Play")
    }

    "render the index page from the application" in {
      val controller = inject[SightingsController]
      val home = controller.index().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Welcome to Play")
    }

    "render the index page from the router" in {
      val request = FakeRequest(GET, "/").withHeaders(FakeHeaders(Seq((Http.HeaderNames.HOST, "localhost:9000"))))
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Welcome to Play")
    }

    "render the sightings page from the router" in {
      val request = FakeRequest(GET, "/sightings").withHeaders(FakeHeaders(Seq((Http.HeaderNames.HOST, "localhost:9000"))))
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/plain")

      contentAsString(home) must include("id")
      contentAsString(home) must include("description")
      contentAsString(home) must include("specie")
      contentAsString(home) must include("sighting made")
      contentAsString(home) must include("count")

    }

    "render the report sightings page from the router" in {
      val url = "/sightings/report"
      val request = FakeRequest(POST, controllers.routes.SightingsController.reportSightings.url).withHeaders(FakeHeaders(Seq((Http.HeaderNames.HOST, "localhost:9000"))))
      val Some(result) =
        route(app, request.withJsonBody(
          Json.obj(
            "id" -> 1,
            "species" -> "redhead",
            "description" -> "very red head",
            "dateTime" -> "2017-10-01T01:01:00Z",
            "count" -> 8)))

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("User successfully added")

    }

    "render the report sightings page without sighting from the router" in {
      val url = "/sightings/report"
      val request = FakeRequest(POST, controllers.routes.SightingsController.reportSightings.url).withHeaders(FakeHeaders(Seq((Http.HeaderNames.HOST, "localhost:9000"))))
      val Some(result) = 
        route(app, request.withJsonBody(Json.obj()))

      status(result) mustBe 400
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("error.path.missing")

    }

    "render the report sightings with wrong specie page from the router" in {
      val url = "/sightings/report"
      val request = FakeRequest(POST, controllers.routes.SightingsController.reportSightings.url).withHeaders(FakeHeaders(Seq((Http.HeaderNames.HOST, "localhost:9000"))))
      val Some(result) = 
        route(app, request.withJsonBody(
          Json.obj(
            "id" -> 1,
            "species" -> "bluehead",
            "description" -> "very red head",
            "dateTime" -> "2017-10-01T01:01:00Z",
            "count" -> 8)))
            
      status(result) mustBe 500
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("\"status\":\"Internal Service error\"")
      contentAsString(result) must include("java.lang.NullPointerException")

    }

  }
}
