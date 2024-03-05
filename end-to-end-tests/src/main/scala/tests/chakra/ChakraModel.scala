package tests.chakra

case class ChakraModel(
    rerun: Boolean = false,
    editableStatus: String = "This will reflect any changes in the form.",
    email: String = "the-test-email@email.com",
    terminate: Boolean = false
)
