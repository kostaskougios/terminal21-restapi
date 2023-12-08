package org.terminal21.client

import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.terminal21.client.ui.{UiElement, UiLib}
import org.terminal21.model.Session
import org.terminal21.ui.std.SessionsService

class ConnectedSession(val session: Session, sessionsService: SessionsService):
  private var usingLibs                                        = List.empty[UiLib]
  def use[T <: UiLib](using factory: ConnectedSession => T): T =
    val lib = factory(this)
    synchronized:
      if usingLibs.contains(lib) then throw new IllegalStateException(s"Please use $lib only once.")
      usingLibs = lib :: usingLibs
    lib

  private var elements = List.empty[UiElement]

  def add(e: UiElement): Unit =
    synchronized:
      elements =
        if elements.exists(_.key == e.key) then
          elements.map: el =>
            if el.key == e.key then e else el
        else e :: elements
    send()

  private def send(): Unit =
    val j = toJson
    sessionsService.setSessionJsonState(session, j.toJson.noSpaces)

  private def toJson: JsonObject =
    val usingLibsCopy = synchronized(usingLibs)
    val elementsCopy  = synchronized(elements).reverse
    val json          = for
      e   <- elementsCopy
      lib <- usingLibsCopy
      j   <- lib.toJson(e)
    yield j
    JsonObject(("elements", json.asJson))
