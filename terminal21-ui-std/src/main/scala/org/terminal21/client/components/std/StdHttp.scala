package org.terminal21.client.components.std

import org.terminal21.client.components.OnChangeEventHandler.CanHandleOnChangeEvent
import org.terminal21.client.components.{Keys, OnChangeEventHandler, UiElement}
import org.terminal21.collections.TypedMap

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
    requestId: String = "cookie-set-req",
    dataStore: TypedMap = TypedMap.empty
) extends StdHttp:
  override type This = Cookie
  override def withKey(key: String): Cookie = copy(key = key)
  override def withDataStore(ds: TypedMap)  = copy(dataStore = ds)

/** Read a cookie value. The value, when read from the ui, it will reflect in `value` assuming the UI had the time to send the value back. Also the onChange
  * handler will be called once with the value.
  */
case class CookieReader(
    key: String = Keys.nextKey,
    name: String = "cookie.name",
    value: Option[String] = None, // will be set when/if cookie value is read
    requestId: String = "cookie-read-req",
    dataStore: TypedMap = TypedMap.empty
) extends StdHttp
    with CanHandleOnChangeEvent:
  type This = CookieReader
  override def withDataStore(ds: TypedMap): CookieReader = copy(dataStore = ds)

  override def withKey(key: String): CookieReader = copy(key = key)
