package tests.chakra

case class ChakraModel(
    rerun: Boolean = false,
    editableStatus: String = "This will reflect any changes in the form.",
    email: String = "the-test-email@email.com",
    breadcrumbStatus: String = "no-breadcrumb-clicked",
    linkStatus: String = "no-link-clicked"
)
