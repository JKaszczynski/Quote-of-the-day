package com.jkaszczynski.quoteoftheday.services

import org.springframework.stereotype.Service

@Service
class QuoteService {

    fun getTodayQuote(): String {
        return getQuoteFromDatabase()
    }

    private fun getQuoteFromDatabase(): String{
        return "hello"
    }

}