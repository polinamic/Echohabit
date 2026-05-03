package com.echohabit.app.core.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class BaseUseCase<in Params, out Result>(
    coroutineContext: CoroutineContext = Dispatchers.Default + SupervisorJob()
) : CoroutineScope {
    
    override val coroutineContext = coroutineContext
    
    abstract suspend fun execute(params: Params): Result
}
