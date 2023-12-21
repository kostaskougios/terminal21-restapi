package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.*

object Icons:
  def components(using session: ConnectedSession): Seq[UiElement] =
    Seq(
      commonBox(text = "Icons"),
      HStack().withChildren(
        InfoIcon(color = Some("tomato")),
        MoonIcon(color = Some("green")),
        AddIcon(),
        ArrowBackIcon(),
        ArrowDownIcon(),
        ArrowForwardIcon(),
        ArrowLeftIcon(),
        ArrowRightIcon(),
        ArrowUpIcon(),
        ArrowUpDownIcon(),
        AtSignIcon(),
        AttachmentIcon(),
        BellIcon(),
        CalendarIcon(),
        ChatIcon(),
        CheckIcon(),
        CheckCircleIcon(),
        ChevronDownIcon(),
        ChevronLeftIcon(),
        ChevronRightIcon(),
        ChevronUpIcon(),
        CloseIcon(),
        CopyIcon(),
        DeleteIcon(),
        DownloadIcon(),
        DragHandleIcon(),
        EditIcon(),
        EmailIcon(),
        ExternalLinkIcon(),
        HamburgerIcon(),
        InfoIcon(),
        InfoOutlineIcon(),
        LinkIcon(),
        LockIcon(),
        MinusIcon(),
        MoonIcon(),
        NotAllowedIcon(),
        PhoneIcon(),
        PlusSquareIcon(),
        QuestionIcon(),
        QuestionOutlineIcon(),
        RepeatIcon(),
        RepeatClockIcon(),
        SearchIcon(),
        Search2Icon(),
        SettingsIcon(),
        SmallAddIcon(),
        SmallCloseIcon(),
        SpinnerIcon(),
        StarIcon(),
        SunIcon(),
        TimeIcon(),
        TriangleDownIcon(),
        TriangleUpIcon(),
        UnlockIcon(),
        UpDownIcon(),
        ViewIcon(),
        ViewOffIcon(),
        WarningIcon(),
        WarningTwoIcon()
      )
    )
