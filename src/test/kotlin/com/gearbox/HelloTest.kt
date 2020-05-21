package com.gearbox

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class HelloTest {

    @Test
    fun `some test` () {
        val a = 2
        a shouldBe 2
    }
}
