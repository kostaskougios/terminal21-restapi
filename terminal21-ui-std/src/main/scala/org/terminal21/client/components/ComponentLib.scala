package org.terminal21.client.components

import io.circe.{Encoder, Json}

trait ComponentLib:
  def toJson(using Encoder[UiElement]): PartialFunction[UiElement, Json]
