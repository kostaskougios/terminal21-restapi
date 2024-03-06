package org.terminal21.client

import functions.fibers.FiberExecutor

given FiberExecutor = FiberExecutor()
val fiberExecutor   = implicitly[FiberExecutor]
