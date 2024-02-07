package org.terminal21.serverapp

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.{ComponentLib, StdElementEncoding, UiElementEncoding}
import org.terminal21.config.Config
import org.terminal21.ui.std.SessionsService

import java.util.concurrent.atomic.AtomicBoolean

class ServerSideSessions(sessionsService: SessionsService):
  def withNewSession[R](id: String, name: String, componentLibs: ComponentLib*)(f: ConnectedSession => R): R =
    val config    = Config.Default
    val serverUrl = s"http://${config.host}:${config.port}"

    val session           = sessionsService.createSession(id, name)
    val encoding          = new UiElementEncoding(Seq(StdElementEncoding) ++ componentLibs)
    val isStopped         = new AtomicBoolean(false)
    def terminate(): Unit =
      isStopped.set(true)

    val connectedSession = ConnectedSession(session, encoding, serverUrl, sessionsService, terminate)
    try
      f(connectedSession)
    finally
      if !isStopped.get() && !connectedSession.isLeaveSessionOpen then sessionsService.terminateSession(session)

trait ServerSideSessionsBeans:
  def sessionsService: SessionsService
  lazy val serverSideSessions = new ServerSideSessions(sessionsService)
