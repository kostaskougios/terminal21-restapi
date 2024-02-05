package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.std.Paragraph
import tests.chakra.Common.commonBox

object Navigation:
  def components(using session: ConnectedSession): Seq[UiElement] =
    val clickedBreadcrumb                  = Paragraph(text = "no-breadcrumb-clicked")
    def breadcrumbClicked(t: String): Unit =
      clickedBreadcrumb.withText(s"breadcrumb-click: $t").renderChanges()

    val clickedLink = Paragraph(text = "no-link-clicked")

    Seq(
      commonBox(text = "Breadcrumbs"),
      Breadcrumb().withChildren(
        BreadcrumbItem().withChildren(
          BreadcrumbLink(text = "breadcrumb-home").onClick(() => breadcrumbClicked("breadcrumb-home"))
        ),
        BreadcrumbItem().withChildren(
          BreadcrumbLink(text = "breadcrumb-link1").onClick(() => breadcrumbClicked("breadcrumb-link1"))
        ),
        BreadcrumbItem(isCurrentPage = Some(true)).withChildren(
          BreadcrumbLink(text = "breadcrumb-link2").onClick(() => breadcrumbClicked("breadcrumb-link2"))
        )
      ),
      clickedBreadcrumb,
      commonBox(text = "Link"),
      Link(text = "link-external-google", href = "https://www.google.com/", isExternal = Some(true))
        .onClick: () =>
          clickedLink.withText("link-clicked").renderChanges(),
      clickedLink
    )
