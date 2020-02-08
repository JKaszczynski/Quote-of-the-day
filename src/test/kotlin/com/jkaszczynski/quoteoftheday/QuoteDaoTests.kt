package com.jkaszczynski.quoteoftheday

import com.jkaszczynski.quoteoftheday.dtos.QuoteBasicInfo
import com.jkaszczynski.quoteoftheday.services.daos.quote.QuoteDao
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.lang.IllegalStateException
import java.time.LocalDate

@SpringBootTest
class QuoteDaoTests(
        @Autowired
        val quoteDao: QuoteDao
) {
    private val quoteText = "test"
    private val quoteId = 1L

    @BeforeEach
    fun persistQuote() {
        val quoteBasicInfo = QuoteBasicInfo(quoteText)
        quoteDao.save(quoteBasicInfo)
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
        val quoteBasicInfo = quoteDao.getByDisplayDate(LocalDate.now())
        Assertions.assertThat(quoteBasicInfo.size).isEqualTo(0)
    }
}