
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import spray.json._

import scala.concurrent.duration._

import scala.concurrent.{ExecutionContextExecutor, Future}

object AkkaExperiments extends App {


  val config = ConfigFactory.load()

  implicit val system: ActorSystem = ActorSystem("systemOfaDown")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val timeout: Timeout = Timeout(10 seconds)

  //Http().singleRequest()


  val route = Route.seal {
    path("helper") {
      //onSuccess(Http().singleRequest())
      complete(200, "yes")

    } ~ path("another") {
      //onSuccess(Http().singleRequest())
      complete(200, "<h1> hello world</h1>")
    }
  }


  val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 8080)


}
