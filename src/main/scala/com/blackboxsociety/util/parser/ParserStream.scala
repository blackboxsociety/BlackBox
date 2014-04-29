package com.blackboxsociety.util.parser

import com.blackboxsociety.net._
import scalaz.syntax.bind._
import scalaz.concurrent._
import com.blackboxsociety.util._
import com.blackboxsociety.util.Finishable
import scala.language.implicitConversions

trait ParserStream {
  val current: Finishable[String]
  def latest: Task[ParserStream]
  def withText(s: Finishable[String]): ParserStream
}

case class TcpParserStream(client: TcpClient, current: Finishable[String]= More("")) extends ParserStream {

  def latest: Task[ParserStream] = {
    client.readAsString() map { l =>
      current match {
        case More(c)     => TcpParserStream(client, l map { s => c + s })
        case c @ Done(_) => TcpParserStream(client, c)
      }
    }
  }

  def withText(s: Finishable[String]): ParserStream =
    TcpParserStream(client, s)

}

object TcpParserStream {

  implicit def TcpClientToParserStream(client: TcpClient): TcpParserStream = TcpParserStream(client)

}