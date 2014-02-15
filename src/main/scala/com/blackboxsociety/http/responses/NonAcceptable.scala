package com.blackboxsociety.http.responses

import com.blackboxsociety.http.HttpResponse

case class NonAcceptable(body: String, headers: List[String] = List()) extends HttpResponse {
  val statusCode: Int = 406
  def make(body: String, headers: List[String]) = NonAcceptable(body, headers)
}