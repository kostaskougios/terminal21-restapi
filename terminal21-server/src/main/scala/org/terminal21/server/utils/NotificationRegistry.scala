package org.terminal21.server.utils

import scala.util.Try

// make sure this doesn't throw any exceptions
type ListenerFunction[A] = A => Boolean

class NotificationRegistry[A]:
  private var ns = List.empty[ListenerFunction[A]]

  def add(listener: ListenerFunction[A]): Unit =
    synchronized:
      ns = listener :: ns

  def addAndNotify(a: A)(listener: ListenerFunction[A]): Unit =
    if listener(a) then add(listener)

  def notifyAll(a: A): Int =
    synchronized:
      ns = ns.filter: f =>
        Try(f(a))
          .recover: e =>
            e.printStackTrace()
            false
          .get
      ns.size
