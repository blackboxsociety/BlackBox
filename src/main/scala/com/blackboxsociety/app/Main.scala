package com.blackboxsociety.app

import com.blackboxsociety.services._
import com.blackboxsociety.app.services._
import com.blackboxsociety.app.middleware.global._
import com.blackboxsociety.mvc.middleware.global._
import com.blackboxsociety.mvc.middleware.general.EtagCacheMiddleware

object Main extends BlackBox {

  override def middleware = List(
    RequestLoggerMiddleware(),
    StaticFileMiddleware("/assets/", "target/resource_managed/main/public/"),
    EtagCacheMiddleware()
  )

  override def port: Int = 3000

  override def host: String = "0.0.0.0"

  override def router = DevServices.router

}
