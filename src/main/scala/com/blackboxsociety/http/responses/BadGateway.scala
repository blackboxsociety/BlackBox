package com.blackboxsociety.http.responses

import com.blackboxsociety.http.HttpResponse

case class BadGateway(body: String, headers: List[String] = List()) extends HttpResponse {
  val statusCode: Int = 502
  def make(body: String, headers: List[String]) = BadGateway(body, headers)
}