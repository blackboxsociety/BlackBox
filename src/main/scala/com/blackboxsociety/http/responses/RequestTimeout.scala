package com.blackboxsociety.http.responses

import com.blackboxsociety.http.{SignedSession, HttpHeader, HttpResponse}
import com.blackboxsociety.app.services.ServiceManifest

case class RequestTimeout(body: String,
                          headers: List[HttpHeader] = List(),
                          session: Option[SignedSession] = None) extends HttpResponse
{
  val statusCode: Int = 408
  def make(body: String, headers: List[HttpHeader], session: Option[SignedSession]) =
    RequestTimeout(body, headers, session)
}
