package tests.chakra

import org.terminal21.client.*
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.std.Paragraph
import tests.chakra.Common.commonBox

object Navigation:
  def components(using Model[Boolean]): Seq[UiElement] =
    val clickedBreadcrumb            = Paragraph(text = "no-breadcrumb-clicked")
    def breadcrumbClicked(t: String) = clickedBreadcrumb.withText(s"breadcrumb-click: $t")

    val clickedLink = Paragraph(text = "no-link-clicked")

    Seq(
      commonBox(text = "Breadcrumbs"),
      Breadcrumb().withChildren(
        BreadcrumbItem().withChildren(
          BreadcrumbLink(text = "breadcrumb-home").onClick: event =>
            import event.*
            handled.withRenderChanges(breadcrumbClicked("breadcrumb-home"))
        ),
        BreadcrumbItem().withChildren(
          BreadcrumbLink(text = "breadcrumb-link1").onClick: event =>
            import event.*
            handled.withRenderChanges(breadcrumbClicked("breadcrumb-link1"))
        ),
        BreadcrumbItem(isCurrentPage = Some(true)).withChildren(
          BreadcrumbLink(text = "breadcrumb-link2").onClick: event =>
            import event.*
            handled.withRenderChanges(breadcrumbClicked("breadcrumb-link2"))
        )
      ),
      clickedBreadcrumb,
      commonBox(text = "Link"),
      Link(text = "link-external-google", href = "https://www.google.com/", isExternal = Some(true))
        .onClick: event =>
          import event.*
          handled.withRenderChanges(clickedLink.withText("link-clicked"))
      ,
      clickedLink
    )
