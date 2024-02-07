package org.terminal21.serverapp

import functions.fibers.FiberExecutor
import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.{ComponentLib, StdElementEncoding, UiElementEncoding}
import org.terminal21.config.Config
import org.terminal21.server.service.ServerSessionsService

import java.util.concurrent.atomic.AtomicBoolean

class ServerSideSessions(sessionsService: ServerSessionsService, executor: FiberExecutor):
  def withNewSession[R](id: String, name: String, componentLibs: ComponentLib*)(f: ConnectedSession => R): R =
    val config    = Config.Default
    val serverUrl = s"http://${config.host}:${config.port}"

    val session           = sessionsService.createSession(id, name)
    val encoding          = new UiElementEncoding(Seq(StdElementEncoding) ++ componentLibs)
    val isStopped         = new AtomicBoolean(false)
    def terminate(): Unit =
      isStopped.set(true)

    val connectedSession = ConnectedSession(session, encoding, serverUrl, sessionsService, terminate)
    sessionsService.notifyMeOnSessionEvents(session): event =>
      executor.submit:
        connectedSession.fireEvent(event)
      true
    try
      f(connectedSession)
    finally
      if !isStopped.get() && !connectedSession.isLeaveSessionOpen then sessionsService.terminateSession(session)

trait ServerSideSessionsBeans:
  def sessionsService: ServerSessionsService
  def fiberExecutor: FiberExecutor
  lazy val serverSideSessions = new ServerSideSessions(sessionsService, fiberExecutor)
