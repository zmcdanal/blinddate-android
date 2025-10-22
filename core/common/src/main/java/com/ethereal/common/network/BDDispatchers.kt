package com.ethereal.common.network

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val bdDispatcher: BDDispatchers)

enum class BDDispatchers {
    Main,
    Default,
    IO,
}