package com.blackboxsociety.http.responses

import com.blackboxsociety.http.HttpResponse

case class MethodNotAllowed(body: String, headers: List[String] = List()) extends HttpResponse {
  val statusCode: Int = 405
  def make(body: String, headers: List[String]) = MethodNotAllowed(body, headers)
}