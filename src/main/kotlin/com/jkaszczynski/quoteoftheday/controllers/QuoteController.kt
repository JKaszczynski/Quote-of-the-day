package com.jkaszczynski.quoteoftheday.controllers

import com.jkaszczynski.quoteoftheday.services.QuoteService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class QuoteController(private val quoteService: QuoteService) {

    @GetMapping
    fun getQuote(): String {
        return quoteService.getTodayQuote()
    }
}