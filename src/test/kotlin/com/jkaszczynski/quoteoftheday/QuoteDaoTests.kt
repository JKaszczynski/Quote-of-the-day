package com.jkaszczynski.quoteoftheday

import com.jkaszczynski.quoteoftheday.dtos.dtos.QuoteDto
import com.jkaszczynski.quoteoftheday.services.daos.quote.QuoteDao
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.lang.IllegalStateException

@SpringBootTest
class QuoteDaoTests(
        @Autowired
        val quoteDao: QuoteDao
) {
    private val quoteText = "test"
    private val quoteId = 1L

    @BeforeEach
    fun persistQuote() {
        val quote = QuoteDto(quoteText)
        quoteDao.save(quote)
    }

    @Test
    fun givenQuoteId_whenPersist_thenFound() {
        val persisted = quoteDao.get(quoteId)
        Assertions.assertThat(persisted.quote).isEqualTo(quoteText)
    }

    @Test
    fun givenQuote_whenUpdated_thenSuccessfullyUpdated() {
        val quoteText = "differentQuote"
        val quote = QuoteDto(quoteText)
        quote.id = quoteId
        quoteDao.update(quote)
        val updated = quoteDao.get(quoteId)
        Assertions.assertThat(updated.quote).isEqualTo(quoteText)
    }

    @Test
    fun whenQuoteRemoved_andAccessingQuote_thenExceptionThrown() {
        quoteDao.remove(quoteId)
        Assertions.assertThatThrownBy { quoteDao.get(quoteId) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessageContaining("null")
    }
}