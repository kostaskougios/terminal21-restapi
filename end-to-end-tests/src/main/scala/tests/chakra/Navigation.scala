package tests.chakra

import org.terminal21.client.*
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.std.Paragraph
import tests.chakra.Common.commonBox

object Navigation:
  def components(using Model[ChakraModel]): Seq[UiElement] =
    val clickedBreadcrumb                            = Paragraph().onModelChange: (p, m) =>
      p.withText(m.breadcrumbStatus)
    def breadcrumbClicked(m: ChakraModel, t: String) = m.copy(breadcrumbStatus = s"breadcrumb-click: $t")

    val clickedLink = Paragraph().onModelChange: (p, m) =>
      p.withText(m.linkStatus)

    Seq(
      commonBox(text = "Breadcrumbs"),
      Breadcrumb().withChildren(
        BreadcrumbItem().withChildren(
          BreadcrumbLink("breadcrumb-home", text = "breadcrumb-home").onClick: event =>
            import event.*
            handled.withModel(breadcrumbClicked(model, "breadcrumb-home"))
        ),
        BreadcrumbItem().withChildren(
          BreadcrumbLink("breadcrumb-link1", text = "breadcrumb-link1").onClick: event =>
            import event.*
            handled.withModel(breadcrumbClicked(model, "breadcrumb-link1"))
        ),
        BreadcrumbItem(isCurrentPage = Some(true)).withChildren(
          BreadcrumbLink("breadcrumb-link2", text = "breadcrumb-link2").onClick: event =>
            import event.*
            handled.withModel(breadcrumbClicked(model, "breadcrumb-link2"))
        )
      ),
      clickedBreadcrumb,
      commonBox(text = "Link"),
      Link(key = "google-link", text = "link-external-google", href = "https://www.google.com/", isExternal = Some(true))
        .onClick: event =>
          import event.*
          handled.mapModel(_.copy(linkStatus = "link-clicked"))
      ,
      clickedLink
    )
