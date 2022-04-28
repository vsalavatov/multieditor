package dev.salavatov.multieditor.expect

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual fun MultifsDispatcher(): CoroutineDispatcher = Dispatchers.IO