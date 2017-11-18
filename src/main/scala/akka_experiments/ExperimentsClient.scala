package akka_experiments

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, Uri}
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.concurrent.duration._
import spray.json._


object ExperimentsClient extends App {

  val config = ConfigFactory.load()

  implicit val system: ActorSystem = ActorSystem("systemOfaUp")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val timeout: Timeout = Timeout(10 seconds)

  val requestToJson = HttpRequest(HttpMethods.GET, Uri("http://localhost:8080/jsonHelper"))
  val requestToHtml = HttpRequest(HttpMethods.GET, Uri("http://localhost:8080/htmlHelper"))

  val jsonString: Future[String] = Http().singleRequest(requestToJson).flatMap(
    res => res.entity.toStrict(10 second).map(_.data.utf8String)
  ).map(a => a.parseJson.asJsObject.fields("name").asInstanceOf[JsString].value)


  val htmlString: Future[String] = Http().singleRequest(requestToHtml).flatMap(
    res => res.entity.toStrict(10 second).map(_.data.utf8String)
  )


  Future.successful(htmlString).flatMap(
    res => res.map(a => println(a))

  )

  Future.successful(jsonString).flatMap(
    res => res.map(a => println(a))
  )


}
