//package org.terminal21.client.components
//
//import functions.fibers.FiberExecutor
//import org.terminal21.client.{ConnectedSession, Model}
//import org.terminal21.client.components.UiElement.HasStyle
//import org.terminal21.client.components.chakra.*
//
//import java.util.concurrent.atomic.{AtomicBoolean, AtomicReference}
//
///** Creates a standard UI for a calculation which may take time. While the calculation runs, the UI is grayed out, including the dataUi component. When the
//  * calculation completes, it allows for updating the dataUi component.
//  * @tparam OUT
//  *   the return value of the calculation.
//  */
//trait StdUiCalculation[OUT](
//    name: String,
//    dataUi: UiElement with HasStyle
//)(using session: ConnectedSession, model: Model[_], executor: FiberExecutor)
//    extends Calculation[OUT]
//    with UiComponent:
//  private val running   = new AtomicBoolean(false)
//  private val currentUi = new AtomicReference(dataUi)
//
//  protected def updateUi(dataUi: UiElement & HasStyle) = currentUi.set(dataUi)
//
//  lazy val badge  = Badge()
//  lazy val recalc = Button(text = "Recalculate", size = Some("sm"), leftIcon = Some(RepeatIcon())).onClick: event =>
//    import event.*
//    if running.compareAndSet(false, true) then
//      try
//        reCalculate()
//      finally running.set(false)
//    handled
//
//  override lazy val rendered: Seq[UiElement] =
//    val header = Box(
//      bg = "green",
//      p = 4,
//      children = Seq(
//        HStack(children = Seq(Text(text = name), badge, recalc))
//      )
//    )
//    Seq(header, dataUi)
//
//  override def onError(t: Throwable): Unit =
//    session.fireEvent(
//      RenderChangesEvent(
//        Seq(
//          badge.withText(s"Error: ${t.getMessage}").withColorScheme(Some("red")),
//          dataUi,
//          recalc.withIsDisabled(None)
//        )
//      )
//    )
//    super.onError(t)
//
//  override protected def whenResultsNotReady(): Unit =
//    session.fireEvent(
//      RenderChangesEvent(
//        Seq(
//          badge.withText("Calculating").withColorScheme(Some("purple")),
//          currentUi.get().withStyle(dataUi.style + ("filter" -> "grayscale(100%)")),
//          recalc.withIsDisabled(Some(true))
//        )
//      )
//    )
//    super.whenResultsNotReady()
//
//  override protected def whenResultsReady(results: OUT): Unit =
//    val newDataUi = currentUi.get().withStyle(dataUi.style - "filter")
//    session.fireEvent(
//      RenderChangesEvent(
//        Seq(
//          badge.withText("Ready").withColorScheme(None),
//          newDataUi,
//          recalc.withIsDisabled(Some(false))
//        )
//      )
//    )
