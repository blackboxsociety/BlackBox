package com.blackboxsociety.app.services.common

import com.blackboxsociety.app.services.interfaces._
import com.blackboxsociety.http._
import com.blackboxsociety.app.services._

trait CommonRoutesComponent extends RoutesComponent { self: ServiceManifest =>

  val router = HttpRouter(
    com.blackboxsociety.app.controllers.index.Get()(this),
    com.blackboxsociety.app.controllers.login.Get()(this),
    com.blackboxsociety.app.controllers.login.Post()(this)
  )

}