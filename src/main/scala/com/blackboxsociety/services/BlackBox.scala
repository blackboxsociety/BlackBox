package com.blackboxsociety.services

import com.blackboxsociety.util._
import scalaz.ImmutableArray
import scalaz.concurrent._
import scalaz.syntax.bind._
import com.blackboxsociety.net._
import com.blackboxsociety.http._
import com.blackboxsociety.util.parser.TcpParserStream._
import java.io._
import com.blackboxsociety.http.HttpRequest
import com.blackboxsociety.http.HttpParserException
import com.blackboxsociety.http.InternalServerError
import com.blackboxsociety.http.MissingRouteException
import com.blackboxsociety.http.Ok
import com.blackboxsociety.http.Missing
import scala.Some
import com.blackboxsociety.app.services.{DevServices}

trait BlackBox {

  val port: Int
  val host: String
  
  def router: HttpRouter

  def genServer(): Task[Unit] = for (
    server <- TcpServer(host, port);
    _      <- Concurrency.forkForever(server.accept() >>= handleRequest)
  ) yield ()

  def servePublicFile(request: HttpRequest): Option[HttpResponse] = {
    if(request.resource.path.startsWith("/assets/")) {
      val src = "target/resource_managed/main/public/" + request.resource.path.substring(8)
      val file = new File(src)
      if(file.exists() && !src.contains("..")) {
        Some(Ok(scala.io.Source.fromFile(file).mkString))
      } else {
        None
      }
    } else {
      None
    }
  }

  def handleRequest(client: TcpClient): Task[Unit] = for (
    response <- parseAndRoute(client).handle(handleError());
    _        <- client.end(HttpResponseConsumer.consume(response))
  ) yield ()

  def handleError(): PartialFunction[Throwable, HttpResponse] = {
    case HttpParserException(e)   =>
      InternalServerError(e)
    case MissingRouteException(r) =>
      servePublicFile(r).getOrElse({ Missing("These are not the droids you're looking for :-/") })
  }

  def parseAndRoute(client: TcpClient): Task[HttpResponse] = for (
    request  <- HttpParser(client);
    response <- router.route(request)
  ) yield response

  def run(args: ImmutableArray[String]) = {
    genServer.runAsync({ _ => Unit})
    EventLoop.run()
  }

}