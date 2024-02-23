package org.terminal21.model

import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.terminal21.client.components.AnyElement

/** These are the events as they arrive from the server
  */
sealed trait CommandEvent:
  def key: String
  def isSessionClosed: Boolean

object CommandEvent:
  def onClick(receivedBy: AnyElement): OnClick                   = OnClick(receivedBy.key)
  def onChange(receivedBy: AnyElement, value: String): OnChange  = OnChange(receivedBy.key, value)
  def onChange(receivedBy: AnyElement, value: Boolean): OnChange = OnChange(receivedBy.key, value.toString)
  def sessionClosed: SessionClosed                               = SessionClosed("-")

  given Encoder[CommandEvent] =
    case c: OnClick        => c.asJson.mapObject(_.add("type", "OnClick".asJson))
    case c: OnChange       => c.asJson.mapObject(_.add("type", "OnChange".asJson))
    case sc: SessionClosed => sc.asJson.mapObject(_.add("type", "SessionClosed".asJson))
    case x                 => throw new IllegalStateException(s"$x should never be send as json")

  given Decoder[CommandEvent] = o =>
    o.get[String]("type") match
      case Right("OnClick")       => o.as[OnClick]
      case Right("OnChange")      => o.as[OnChange]
      case Right("SessionClosed") => o.as[SessionClosed]
      case x                      => throw new IllegalStateException(s"got unexpected $x")

case class OnClick(key: String) extends CommandEvent:
  override def isSessionClosed: Boolean = false

case class OnChange(key: String, value: String) extends CommandEvent:
  override def isSessionClosed: Boolean = false

case class SessionClosed(key: String) extends CommandEvent:
  override def isSessionClosed: Boolean = true

/** Extend this to send your own messages
  */
trait ClientEvent extends CommandEvent:
  override def key = "client-event"
