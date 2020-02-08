package com.jkaszczynski.quoteoftheday

import com.jkaszczynski.quoteoftheday.services.QuoteService
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
        val quoteService: QuoteService,
        @Autowired
        val jdbcTemplate: JdbcTemplate) {

    private val quoteText = "test"

    @BeforeEach
    fun persistQuote() {
        cleanDatabase()
        insertQuote(1L, quoteText, null)
    }

    private fun insertQuote(id: Long, quote: String, displayedDate: LocalDate?) {
        jdbcTemplate.update("INSERT INTO Quotes VALUES(?,?,?,?,?)", id, null, displayedDate, null, quote)
    }

    fun cleanDatabase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "Quotes")
        jdbcTemplate.update("ALTER TABLE Quotes ALTER COLUMN id RESTART WITH 1")
    }

    @Test
    fun whenObtainingQuote_thenValueReturned() {
        val quote = quoteService.getTodayQuote()
        Assertions.assertThat(quote).isNotBlank()
    }

    @Test
    fun givenNeverDisplayedQuote_whenObtainingQuote_thenQuoteReturned() {
        val todayQuote = quoteService.getTodayQuote()
        Assertions.assertThat(todayQuote).isEqualTo(quoteText)
    }

    @Test
    fun givenTwoQuotes_whenObtainingQuoteTwice_thenSameQuoteReturned() {
        quoteService.getTodayQuote()
        insertQuote(2L, "differentQuote", null)
        val todayQuote = quoteService.getTodayQuote()
        Assertions.assertThat(todayQuote).isEqualTo(quoteText)
    }

    @Test
    fun givenAdditionalQuoteWithOldDate_whenObtainingQuote_newQuoteReturned() {
        insertQuote(2L, "differentQuote", getYesterdayDate())
        val todayQuote = quoteService.getTodayQuote()
        Assertions.assertThat(todayQuote).isEqualTo(quoteText)
    }

    private fun getYesterdayDate(): LocalDate {
        return LocalDate.now().minusDays(1)
    }

    @Test
    fun givenTwoOldQuotes_whenObtainingQuote_thenOldQuoteReturned() {
        updateDateToDayBefore()
        insertQuote(2L, "differentQuote", getYesterdayDate())
        val todayQuote = quoteService.getTodayQuote()
        Assertions.assertThat(todayQuote).isNotBlank()
    }

    private fun updateDateToDayBefore() {
        jdbcTemplate.update("UPDATE Quotes SET displayed_date = ? WHERE id = 1L", getYesterdayDate())
    }
}