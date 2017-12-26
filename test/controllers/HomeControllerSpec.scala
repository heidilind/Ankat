package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "SightingsController GET" should {

    "render the index page from a new instance of controller" in {
      val controller = new SightingsController(stubControllerComponents())
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
      val request = FakeRequest(GET, "/")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Welcome to Play")
    }

    "render the sightings page from the router" in {
      val request = FakeRequest(GET, "/sightings")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
      contentAsString(home) must include("id")
      contentAsString(home) must include("description")
      contentAsString(home) must include("species")
      contentAsString(home) must include("dateTime")
      contentAsString(home) must include("count")
    }

    "render the report sightings page from the router" in {
      val url = "/sightings/report"
      val request = FakeRequest(POST, controllers.routes.SightingsController.reportSightings.url)
      val Some(result) =
        route(app, request.withJsonBody(
          Json.obj(
            "id" -> "1",
            "species" -> "redhead",
            "description" -> "very red head",
            "dateTime" -> "2017-10-01T01:01:00Z",
            "count" -> 8)))

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("The request contained JSON data")
      contentAsString(result) must include("id")
    }

    "render the report NO sightings page from the router" in {
      val url = "/sightings/report"
      val request = FakeRequest(POST, controllers.routes.SightingsController.reportSightings.url)
      val Some(result) = route(app, request.withTextBody(""))
      
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("The request contained no JSON data")

    }

  }
}
