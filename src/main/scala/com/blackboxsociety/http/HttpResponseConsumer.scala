package com.blackboxsociety.http

object HttpResponseConsumer {

  def consume(response: HttpResponse): String = {
    val statusLine        = genStatusLine(response)
    val headerLines       = genHeaderLines(response)
    val sessionLine       = genSessionHeaderLines(response).getOrElse("")
    val flashLine         = genFlashHeaderLines(response)
    val contentLengthLine = genContentLengthLine(response)
    val bodyClause        = genBody(response)

    s"$statusLine$headerLines$sessionLine$flashLine$contentLengthLine$bodyClause"
  }

  private def genStatusLine(response: HttpResponse): String = {
    s"HTTP/1.1 ${response.statusCode} OK\r\n"
  }

  private def genHeaderLines(response: HttpResponse): String = {
    if (response.headers.size > 0) {
      response.headers.map(_.toString).mkString("\r\n") + "\r\n"
    } else {
      ""
    }
  }

  private def genSessionHeaderLines(response: HttpResponse): Option[String] = {
    response.session map { s =>
      SetCookieHeader(s"session=${s.signature()}${s.toJson}; Path=/; HttpOnly").toString + "\r\n"
    }
  }

  private def genFlashHeaderLines(response: HttpResponse): String = {
    response.flash match {
      case Some(f) => SetCookieHeader(s"flash=${f.signature()}${f.toJson}; Path=/; HttpOnly").toString + "\r\n"
      case None    =>
        if (response.statusCode == 404 || response.statusCode == 500) // TODO: this is hacky should be more specific
          ""
        else
          SetCookieHeader(s"flash=none; Path=/; HttpOnly").toString + "\r\n"
    }
  }

  private def genContentLengthLine(response: HttpResponse): String = {
    ContentLengthHeader(response.body.length.toString).toString + "\r\n"
  }

  private def genBody(response: HttpResponse): String = {
    s"\r\n${response.body}"
  }

}
