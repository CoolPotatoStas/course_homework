package my.examples.curse_homework

import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext

suspend fun hello(): Int {
    println("Вызван кусок block у child")
    return 0
}

suspend fun rootHello(): LazyCustomDeferredImage<Int> {
    println("Вызван кусок block у root")
    return LazyCustomDeferredImage<Int>(
        EmptyCoroutineContext,
        listOf(),
        ::hello
    )
}

@OptIn(DelicateCoroutinesApi::class)
suspend fun main(): Unit = coroutineScope {

    val child1 = LazyCustomDeferredImage<Int>(
        EmptyCoroutineContext,
        listOf(),
        ::hello
    )
    val child2 = LazyCustomDeferredImage<Int>(
        EmptyCoroutineContext,
        listOf(),
        ::hello
    )

    val root: LazyCustomDeferredImage<LazyCustomDeferredImage<Int>> =
        LazyCustomDeferredImage<LazyCustomDeferredImage<Int>>(
            EmptyCoroutineContext,
            listOf(
                child1,
                child2
            ),
            ::rootHello
        )

    val result = root.async(GlobalScope).await()

}

