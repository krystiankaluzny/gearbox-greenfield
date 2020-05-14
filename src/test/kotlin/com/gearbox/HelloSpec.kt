package com.gearbox

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe


class HelloSpec : StringSpec({

    "hello" {
        val a = 2
        a shouldBe 2
    }
})
