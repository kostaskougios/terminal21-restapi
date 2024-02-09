package org.terminal21.client.components.std

import org.terminal21.client.components.{Keys, UiElement}

/** Elements mapping to Http functionality
  */
sealed trait StdHttp extends UiElement

case class Cookie(
    key: String = Keys.nextKey,
    name: String = "cookie.name",
    value: String = "cookie.value",
    path: Option[String] = None,
    expireDays: Option[Int] = None
) extends StdHttp
