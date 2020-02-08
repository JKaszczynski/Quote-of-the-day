package com.jkaszczynski.quoteoftheday

import com.jkaszczynski.quoteoftheday.dtos.QuoteBasicInfo
import com.jkaszczynski.quoteoftheday.services.daos.quote.QuoteDao
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.jdbc.JdbcTestUtils
import java.lang.IllegalStateException
import java.time.LocalDate

@SpringBootTest
class QuoteDaoTests(
        @Autowired
        val quoteDao: QuoteDao,

        @Autowired
        val jdbcTemplate: JdbcTemplate
) {
    private val quoteText = "test"
    private val quoteId = 1L

    @BeforeEach
    fun persistQuote() {
        cleanDatabase()
        val quoteBasicInfo = QuoteBasicInfo(quoteText)
        quoteDao.save(quoteBasicInfo)
    }

    fun cleanDatabase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "Quotes")
        jdbcTemplate.update("ALTER TABLE Quotes ALTER COLUMN id RESTART WITH 1")
    }

    @Test
    fun givenQuoteId_whenPersist_thenFound() {
        val persisted = quoteDao.get(quoteId)
        Assertions.assertThat(persisted.quote).isEqualTo(quoteText)
    }

    @Test
    fun givenQuote_whenUpdated_thenSuccessfullyUpdated() {
        val differentQuoteText = "differentQuote"
        val quoteBasicInfo = QuoteBasicInfo(differentQuoteText)
        quoteBasicInfo.id = quoteId
        quoteDao.update(quoteBasicInfo)
        val updated = quoteDao.get(quoteId)
        Assertions.assertThat(updated.quote).isEqualTo(differentQuoteText)
    }

    @Test
    fun whenQuoteRemoved_andAccessingQuote_thenExceptionThrown() {
        quoteDao.remove(quoteId)
        Assertions.assertThatThrownBy { quoteDao.get(quoteId) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessageContaining("null")
    }

    @Test
    fun givenQuoteWithoutDisplayedDate_whenSearchByDate_thenNotFound() {
        val quotesBasicInfo = quoteDao.getByDisplayDate(LocalDate.now())
        Assertions.assertThat(quotesBasicInfo.size).isEqualTo(0)
    }

    @Test
    fun givenQuoteWithoutDisplayedDate_whenSearchNotDisplayedYet_thenFound() {
        val quotesBasicInfo = quoteDao.getNotDisplayed()
        Assertions.assertThat(quotesBasicInfo.size).isEqualTo(1)
    }

    @Test
    fun givenNeverDisplayedQuote_whenDisplayed_thenSetDisplayDate() {
        val quotesBasicInfo = quoteDao.get(quoteId)
        quoteDao.setDisplayDate(quotesBasicInfo)
        val updatedQuote = quoteDao.getQuote(quoteId)

        Assertions.assertThat(updatedQuote.displayedDate).isEqualTo(LocalDate.now())
    }

    @Test
    fun whenObtainingRandomQuote_thenQuoteReturned() {
        val secondQuote = QuoteBasicInfo("test2")
        quoteDao.save(secondQuote)
        val quote = quoteDao.getRandom()
        Assertions.assertThat(quote.quote).isNotBlank()
    }


}