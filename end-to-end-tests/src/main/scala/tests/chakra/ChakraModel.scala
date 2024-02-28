package tests.chakra

case class ChakraModel(
    rerun: Boolean = false,
    box1: String = "Clicks will be reported here.",
    editableStatus: String = "This will reflect any changes in the form.",
    formStatus: String = "This will reflect any changes in the form.",
    email: String = "the-test-email@email.com",
    breadcrumbStatus: String = "no-breadcrumb-clicked",
    linkStatus: String = "no-link-clicked"
)
