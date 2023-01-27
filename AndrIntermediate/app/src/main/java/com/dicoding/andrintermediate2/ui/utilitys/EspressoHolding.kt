package com.dicoding.andrintermediate2.ui.utilitys

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoHolding {
    private const val RESOURCE = "GLOBAL"

    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}

inline fun <T> wrapEspressoIdling(function: () -> T): T {
    EspressoHolding.increment()
    return try {
        function()
    } finally {
        EspressoHolding.decrement()
    }
}