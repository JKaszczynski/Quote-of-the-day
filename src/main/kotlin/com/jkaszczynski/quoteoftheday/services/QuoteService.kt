package com.jkaszczynski.quoteoftheday.services

import com.jkaszczynski.quoteoftheday.dtos.QuoteBasicInfo
import com.jkaszczynski.quoteoftheday.services.daos.quote.QuoteDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class QuoteService(
        @Autowired
        val quoteDao: QuoteDao
) {

    fun getTodayQuote(): String {
        return getQuoteFromDatabase().quote
    }

    private fun getQuoteFromDatabase(): QuoteBasicInfo {
        val quotesDisplayedToday = quoteDao.getByDisplayDate(LocalDate.now())
        return if (quotesDisplayedToday.isNotEmpty()) {
            quotesDisplayedToday[0]
        } else {
            getNewQuote()
        }
    }

    private fun getNewQuote(): QuoteBasicInfo {
        val quotesNeverDisplayed = quoteDao.getNotDisplayed()
        val quote: QuoteBasicInfo
        quote = if (quotesNeverDisplayed.isNotEmpty()) {
            quotesNeverDisplayed[0]
        } else {
            quoteDao.getRandom()
        }
        quoteDao.setDisplayDate(quote)
        return quote
    }
}