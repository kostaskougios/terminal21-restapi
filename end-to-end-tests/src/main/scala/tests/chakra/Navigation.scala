package tests.chakra

import org.terminal21.client.*
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.std.Paragraph
import tests.chakra.Common.commonBox

object Navigation:
  def components(events: Events): Seq[UiElement] =
    val bcLink1 = BreadcrumbLink("breadcrumb-home", text = "breadcrumb-home")
    val bcLink2 = BreadcrumbLink("breadcrumb-link1", text = "breadcrumb1")
    val bcLink3 = BreadcrumbItem(isCurrentPage = Some(true))
    val bcLink4 = BreadcrumbLink("breadcrumb-link2", text = "breadcrumb2")
    val link    = Link(key = "google-link", text = "link-external-google", href = "https://www.google.com/", isExternal = Some(true))

    val bcStatus =
      (
        events.ifClicked(bcLink1, "breadcrumb-home").toSeq ++
          events.ifClicked(bcLink2, "breadcrumb-link1") ++
          events.ifClicked(bcLink3, "breadcrumb-link2") ++
          events.ifClicked(bcLink4, "breadcrumb-link2")
      ).headOption.getOrElse("no-breadcrumb-clicked")

    val clickedBreadcrumb = Paragraph(text = bcStatus)
    val clickedLink       = Paragraph(text = if events.isClicked(link) then "link-clicked" else "no-link-clicked")

    Seq(
      commonBox(text = "Breadcrumbs"),
      Breadcrumb().withChildren(
        BreadcrumbItem().withChildren(bcLink1),
        BreadcrumbItem().withChildren(bcLink2),
        bcLink3.withChildren(bcLink3)
      ),
      clickedBreadcrumb,
      commonBox(text = "Link"),
      link,
      clickedLink
    )
