package org.terminal21.client.components.std

import org.terminal21.client.{ConnectedSession, EventHandler, OnChangeEventHandler}
import org.terminal21.client.components.UiElement.HasEventHandler
import org.terminal21.client.components.{Keys, TransientRequest, UiElement}
import org.terminal21.model.OnChange

/** Elements mapping to Http functionality
  */
sealed trait StdHttp extends UiElement:
  /** Each requestId will be processed only once per browser.
    *
    * I.e. lets say we have the Cookie(). If we add a cookie, we send it to the UI which in turn checks if it already set the cookie via the requestId. If it
    * did, it skips it, if it didn't it sets the cookie.
    *
    * @return
    *   Should always be TransientRequest.newRequestId()
    */
  def requestId: String

/** On the browser, https://github.com/js-cookie/js-cookie is used.
  *
  * Set a cookie on the browser.
  */
case class Cookie(
    key: String = Keys.nextKey,
    name: String = "cookie.name",
    value: String = "cookie.value",
    path: Option[String] = None,
    expireDays: Option[Int] = None,
    requestId: String = TransientRequest.newRequestId()
) extends StdHttp

/** Read a cookie value. The value, when read from the ui, it will reflect in `value` assuming the UI had the time to send the value back. Also the onChange
  * handler will be called once with the value.
  */
case class CookieReader(
    key: String = Keys.nextKey,
    name: String = "cookie.name",
    value: Option[String] = None, // will be set when/if cookie value is read
    requestId: String = TransientRequest.newRequestId()
) extends StdHttp
    with HasEventHandler:
  override def defaultEventHandler(session: ConnectedSession): OnChangeEventHandler = newValue => session.modified(copy(value = Some(newValue)))

  def onChange(h: OnChangeEventHandler)(using session: ConnectedSession): CookieReader =
    session.addEventHandler(key, h)
    this
