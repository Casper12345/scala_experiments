package akka_experiments

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import spray.json._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContextExecutor, Future}

object ExperimentsServer extends App {


  val config = ConfigFactory.load()

  implicit val system: ActorSystem = ActorSystem("systemOfaDown")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val timeout: Timeout = Timeout(10 seconds)


  val route = Route.seal {
    path("jsonHelper") {

      val jsObject: JsObject = """{"dadel": 23, "name": "Paul"}""".parseJson.asJsObject

      val resp = HttpResponse(StatusCodes.OK, entity = HttpEntity(ContentTypes.`application/json`, jsObject.compactPrint))

      complete(resp)

    } ~ path("htmlHelper") {

      val htmlString = """<h1> hello world</h1>"""

      val resp = HttpResponse(StatusCodes.OK, entity = HttpEntity(ContentTypes.`text/html(UTF-8)`, htmlString))

      complete(resp)

    } ~ path("xmlHelper") {

      val xmlString = """<myTag> hello world </myTag>"""

      val resp = HttpResponse(StatusCodes.OK, entity = HttpEntity(ContentTypes.`text/xml(UTF-8)`, xmlString))

      complete(resp)
    }
  }

  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "0.0.0.0", 8080)

}
