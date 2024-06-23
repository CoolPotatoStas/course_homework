package my.examples.curse_homework

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job

interface CustomDeferredImage<out T> {

    val children: List<CustomDeferredImage<*>>

    val deferred: Deferred<T>?

    fun async(coroutineScope: CoroutineScope): Deferred<T>

    fun launch(coroutineScope: CoroutineScope): Job

    suspend fun await(): T

    fun reset()

    fun isFinished(): Boolean
}