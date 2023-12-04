package scripts

import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.terminal21.server.json.{SessionId, WsRequest}

@main def examineJson() =
  println(WsRequest("init", Some(SessionId("123"))).asJson.noSpaces)
