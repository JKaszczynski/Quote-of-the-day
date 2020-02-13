package com.jkaszczynski.quoteoftheday.services

import com.jkaszczynski.quoteoftheday.dtos.QuoteBasicInfo
import com.jkaszczynski.quoteoftheday.services.daos.quote.QuoteDao
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class QuoteService(
        private val quoteDao: QuoteDao
) {
    private val quoteCache: HashMap<LocalDate, QuoteBasicInfo> = HashMap()

    fun getTodayQuote(): QuoteBasicInfo {
        val cachedQuote = getQuoteFromCache()
        return if (cachedQuote.quote.isNotBlank()) {
            cachedQuote
        } else {
            clearCacheHistory()
            addToCacheAndReturn(getQuoteFromDatabase())
        }
    }

    private fun getQuoteFromCache(): QuoteBasicInfo {
        return quoteCache.getOrDefault(LocalDate.now(), QuoteBasicInfo(""))
    }

    private fun clearCacheHistory() {
        quoteCache.clear()
    }

    private fun addToCacheAndReturn(quoteBasicInfo: QuoteBasicInfo): QuoteBasicInfo {
        quoteCache[LocalDate.now()] = quoteBasicInfo
        return quoteBasicInfo
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