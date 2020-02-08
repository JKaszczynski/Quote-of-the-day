package com.jkaszczynski.quoteoftheday

import com.jkaszczynski.quoteoftheday.services.QuoteService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class QuoteServiceTests(
        @Autowired
        val quoteService: QuoteService) {

    @Test
    fun whenObtainingQuote_thenValueReturned() {
        val quote = quoteService.getTodayQuote()
        Assertions.assertThat(quote.length).isGreaterThan(0)
    }
}