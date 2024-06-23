package my.examples.curse_homework

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import mu.KotlinLogging

class LazyCustomDeferredImage<T>(
    private val coroutineContext: CoroutineContext = EmptyCoroutineContext,
    override val children: List<LazyCustomDeferredImage<*>> = emptyList(),
    val block: suspend () -> T
) : CustomDeferredImage<T> {

    override var deferred: Deferred<T>? = null
        private set

    private val logger = KotlinLogging.logger {}

    override fun async(coroutineScope: CoroutineScope): Deferred<T> {

        logger.info {
            "${this@LazyCustomDeferredImage}: running dependency"
        }

        val startedChildren = children.map {
            it.async(coroutineScope)
        }

        return coroutineScope.async(
            coroutineContext
            + Dependencies(startedChildren),
            CoroutineStart.LAZY
        )
        {
            startedChildren.forEach {deferred ->
                deferred.invokeOnCompletion {
                        error -> error?: {
                            logger.error {
                                "${this@LazyCustomDeferredImage}: ${error?.message}"
                            }
                            this.cancel()
                    }
                }
                deferred.await()
            }
            block()
        }.also {
            deferred = it
            logger.info {
                "${this@LazyCustomDeferredImage}: dependency startup completed"
            }
        }
    }

    override fun launch(coroutineScope: CoroutineScope): Job {
        return async(coroutineScope)
    }

    override suspend fun await(): T = coroutineScope {
        async(this).await()
    }

    override fun reset() {
        deferred?.cancel()
    }

    override fun isFinished(): Boolean {
        return deferred?.isCompleted ?: false
    }

}