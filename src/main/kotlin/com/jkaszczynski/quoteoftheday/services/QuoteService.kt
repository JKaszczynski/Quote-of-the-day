package com.jkaszczynski.quoteoftheday.services

import com.jkaszczynski.quoteoftheday.dtos.QuoteBasicInfo
import com.jkaszczynski.quoteoftheday.services.daos.quote.QuoteDao
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class QuoteService(
        private val quoteDao: QuoteDao
) {
    private val quoteCache: HashMap<LocalDate, String> = HashMap()

    fun getTodayQuote(): String {
        return getQuoteFromCache().ifEmpty {
            clearCacheHistory()
            addToCacheAndReturn(getQuoteFromDatabase())
        }
    }

    private fun getQuoteFromCache(): String {
        return quoteCache.getOrDefault(LocalDate.now(), "")
    }

    private fun clearCacheHistory() {
        quoteCache.clear()
    }

    private fun addToCacheAndReturn(quoteBasicInfo: QuoteBasicInfo): String {
        quoteCache[LocalDate.now()] = quoteBasicInfo.quote
        return quoteBasicInfo.quote
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
        val quote = if (quotesNeverDisplayed.isNotEmpty()) {
            quotesNeverDisplayed[0]
        } else {
            quoteDao.getRandom()
        }
        quoteDao.updateDisplayDate(quote)
        return quote
    }
}