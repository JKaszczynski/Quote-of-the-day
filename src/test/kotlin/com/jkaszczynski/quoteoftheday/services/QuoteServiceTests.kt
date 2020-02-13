package com.jkaszczynski.quoteoftheday.services

import com.jkaszczynski.quoteoftheday.cleanDatabase
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.jdbc.JdbcTestUtils
import java.time.LocalDate

@SpringBootTest
class QuoteServiceTests(
        @Autowired
        private val quoteService: QuoteService,
        @Autowired
        private val jdbcTemplate: JdbcTemplate) {

    private val quoteText = "test"
    private val quoteAuthor = "testAuthor"

    @BeforeEach
    fun persistQuote() {
        cleanDatabase(jdbcTemplate, "Quotes")
        insertQuote(1L, quoteText, quoteAuthor, null)
    }

    private fun insertQuote(id: Long, quote: String, author: String, displayedDate: LocalDate?) {
        jdbcTemplate.update("INSERT INTO Quotes VALUES(?,?,?,?,?,?)", id, author, null, displayedDate, null, quote)
    }

    @Test
    fun whenObtainingQuote_thenValueReturned() {
        val quote = quoteService.getTodayQuote()
        Assertions.assertThat(quote.quote).isNotBlank()
    }

    @Test
    fun givenNeverDisplayedQuote_whenObtainingQuote_thenQuoteReturned() {
        val todayQuote = quoteService.getTodayQuote().quote
        Assertions.assertThat(todayQuote).isEqualTo(quoteText)
    }

    @Test
    fun givenTwoQuotes_whenObtainingQuoteTwice_thenSameQuoteReturned() {
        quoteService.getTodayQuote().quote
        insertQuote(2L, "differentQuote", "differentAuthor", null)
        val todayQuote = quoteService.getTodayQuote().quote
        Assertions.assertThat(todayQuote).isEqualTo(quoteText)
    }

    @Test
    fun givenAdditionalQuoteWithOldDate_whenObtainingQuote_newQuoteReturned() {
        insertQuote(2L, "differentQuote", "differentAuthor", getYesterdayDate())
        val todayQuote = quoteService.getTodayQuote().quote
        Assertions.assertThat(todayQuote).isEqualTo(quoteText)
    }

    private fun getYesterdayDate(): LocalDate {
        return LocalDate.now().minusDays(1)
    }

    @Test
    fun givenTwoOldQuotes_whenObtainingQuote_thenOldQuoteReturned() {
        updateDateToDayBefore()
        insertQuote(2L, "differentQuote", "differentAuthor", getYesterdayDate())
        val todayQuote = quoteService.getTodayQuote()
        Assertions.assertThat(todayQuote.quote).isNotBlank()
    }

    private fun updateDateToDayBefore() {
        jdbcTemplate.update("UPDATE Quotes SET displayed_date = ? WHERE id = 1L", getYesterdayDate())
    }

    @Test
    fun whenObtainingQuoteAndThenDeletingFromDatabase_thenReturnQuoteFromCache() {
        val beforeDeleting = quoteService.getTodayQuote().quote
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "Quotes")
        val quoteFromCache = quoteService.getTodayQuote().quote
        Assertions.assertThat(quoteFromCache).isNotBlank()
        Assertions.assertThat(quoteFromCache).isEqualTo(beforeDeleting)
    }
}