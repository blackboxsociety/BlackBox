package com.blackboxsociety.http.responses

import com.blackboxsociety.http.HttpResponse

case class ServiceUnavailable(body: String, headers: List[String] = List()) extends HttpResponse {
  val statusCode: Int = 503
  def make(body: String, headers: List[String]) = ServiceUnavailable(body, headers)
}