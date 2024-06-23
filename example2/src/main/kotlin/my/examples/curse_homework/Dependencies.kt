package my.examples.curse_homework

import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class Dependencies(
    val children: Collection<out Job>
) : CoroutineContext.Element {

    override val key: CoroutineContext.Key<*>
        get() = Dependencies


    companion object : CoroutineContext.Key<Dependencies>
}