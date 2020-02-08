package com.jkaszczynski.quoteoftheday

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class QuoteOfTheDayApplication

fun main(args: Array<String>) {
    runApplication<QuoteOfTheDayApplication>(*args)
}
