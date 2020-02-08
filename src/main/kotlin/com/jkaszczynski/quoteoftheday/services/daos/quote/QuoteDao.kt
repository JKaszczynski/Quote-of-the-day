package com.jkaszczynski.quoteoftheday.services.daos.quote

import com.jkaszczynski.quoteoftheday.dtos.dtos.QuoteDto
import com.jkaszczynski.quoteoftheday.entities.Quote
import com.jkaszczynski.quoteoftheday.services.daos.Dao
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@Service
@Transactional
class QuoteDao(
        @PersistenceContext
        val entityManager: EntityManager
) : Dao<QuoteDto, Long> {

    override fun get(id: Long): QuoteDto {
        val quote = getQuote(id)
        return asDto(quote)
    }

    private fun getQuote(id: Long): Quote {
        return entityManager.find(Quote::class.java, id)
    }

    private fun asDto(quote: Quote): QuoteDto {
        val quoteDto = QuoteDto(quote.quote)
        quoteDto.id = quote.id
        return quoteDto
    }

    override fun save(entity: QuoteDto) {
        Assert.hasText(entity.quote, "Cannot persist quote with no text")
        val newQuote = Quote(entity.quote)
        entityManager.persist(newQuote)
    }

    override fun remove(id: Long) {
        val quote = getQuote(id)
        entityManager.remove(quote)
    }

    override fun update(entity: QuoteDto) {
        Assert.hasText(entity.quote, "Cannot update quote if no text was provided")
        val quote = getQuote(entity.id)
        quote.quote = entity.quote
        entityManager.merge(quote)
    }
}