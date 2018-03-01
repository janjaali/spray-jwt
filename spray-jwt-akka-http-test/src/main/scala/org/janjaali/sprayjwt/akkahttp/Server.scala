package org.janjaali.sprayjwt.akkahttp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import org.janjaali.sprayjwt.akkahttp.token.TokenRoutes

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object Server extends LazyLogging {
  def main(args: Array[String]): Unit = {
    logger.info("Starting spray-jwt-akka-http Server")

    implicit val system: ActorSystem = ActorSystem("spray-jwt-akka-http-test")
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer.create(system)

    val config = ConfigFactory.defaultApplication()

    val host = "0.0.0.0"
    val port = config.getInt("server.port")

    val tokenRoutes = new TokenRoutes

    val routes = new ApiRoutes(
      tokenRoutes.routes
    ).route

    Http().bindAndHandle(routes, host, port)
      .onComplete {
        case Success(_) => logger.info(s"Server starter on $host:$port")
        case Failure(ex) =>
          logger.error(s"Server failed to start due to ${ex.getMessage}", ex)
          System.exit(1)
      }
  }
}
