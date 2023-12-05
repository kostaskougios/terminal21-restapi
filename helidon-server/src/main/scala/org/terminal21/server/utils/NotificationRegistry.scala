package org.terminal21.server.utils

// make sure this doesn't throw any exceptions
type ListenerFunction[A] = A => Boolean

class NotificationRegistry[A]:
  private var ns = List.empty[ListenerFunction[A]]

  def addAndNotify(a: A)(listener: ListenerFunction[A]): Unit =
    if listener(a) then
      synchronized:
        ns = listener :: ns

  def notifyAll(a: A): Int =
    synchronized:
      ns = ns.filter(f => f(a))
      ns.size
