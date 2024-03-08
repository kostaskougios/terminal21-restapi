package tests.chakra

import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.commonBox

object Feedback:
  def components: Seq[UiElement] =
    Seq(
      commonBox(text = "Alerts"),
      VStack().withChildren(
        Alert(status = "error").withChildren(AlertIcon(), AlertTitle(text = "Alert:error"), AlertDescription(text = "alert-error-text-01")),
        Alert(status = "success").withChildren(AlertIcon(), AlertTitle(text = "Alert:success"), AlertDescription(text = "alert-success-text-01")),
        Alert(status = "warning").withChildren(AlertIcon(), AlertTitle(text = "Alert:warning"), AlertDescription(text = "alert-warning-text-01")),
        Alert(status = "info").withChildren(AlertIcon(), AlertTitle(text = "Alert:info"), AlertDescription(text = "alert-info-text-01"))
      ),
      commonBox(text = "Progress"),
      Progress(value = 10),
      Progress(value = 20, hasStripe = Some(true)),
      Progress(value = 30, isIndeterminate = Some(true)),
      Tooltip(label = "A help message").withContent(Text(text = "hover me!"))
    )
