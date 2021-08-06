import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.Promise

import com.typesafe.config.ConfigFactory

import akka.Done
import akka.actor._

import akka.http._
import akka.http.scaladsl._
import akka.http.scaladsl.model._

given system: ActorSystem = ActorSystem(
  "SingleRequest",
  ConfigFactory.parseString("""
    akka {
      loglevel = info
      http.host-connection-pool {
        max-retries = 0
        base-connection-backoff = 0ms
        max-connection-backoff = 0ms
      }
    }
  """)
)
given ExecutionContext = system.dispatcher

// Always succeeds
def performTimedRequest(): Future[Done] =
  system.log.info("Next request")
  val startTime = System.currentTimeMillis()

  val done = Promise[Done]()

  Http()
    .singleRequest(HttpRequest(uri = "https://fake.url:9000"))
    .onComplete { c =>
      system.log.info(s"Result $c in ${System.currentTimeMillis() - startTime} ms")
      done.success(Done)
    }

  done.future

object Main extends App:
  system.log.info("Requesting...")

  for {
    _ <- performTimedRequest()
    _ <- performTimedRequest()
    _ <- performTimedRequest()
  } yield {
    system.terminate()
  }
