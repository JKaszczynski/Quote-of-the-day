package com.jkaszczynski.quoteoftheday.services.daos.quote

import com.jkaszczynski.quoteoftheday.dtos.QuoteBasicInfo
import com.jkaszczynski.quoteoftheday.entities.Quote
import com.jkaszczynski.quoteoftheday.services.daos.Dao
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.time.LocalDate
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional
import kotlin.random.Random

@Service
@Transactional
class QuoteDao(
        @PersistenceContext
        private val entityManager: EntityManager
) : Dao<QuoteBasicInfo, Long> {

    override fun get(id: Long): QuoteBasicInfo {
        val quote = getQuote(id)
        return asDto(quote)
    }

    fun getQuote(id: Long): Quote {
        return entityManager.find(Quote::class.java, id)
    }

    private fun asDto(quote: Quote): QuoteBasicInfo {
        val quoteBasicInfo = QuoteBasicInfo(quote.quote)
        quoteBasicInfo.id = quote.id
        return quoteBasicInfo
    }

    fun getByDisplayDate(date: LocalDate): List<QuoteBasicInfo> {
        val query = entityManager.createNamedQuery("Quote.getByDisplayDate", QuoteBasicInfo::class.java)
        query.setParameter("date", date)
        return query.resultList
    }

    fun getNotDisplayed(): List<QuoteBasicInfo> {
        val query = entityManager.createNamedQuery("Quote.getNotDisplayed", QuoteBasicInfo::class.java)
        return query.resultList
    }

    fun getRandom(): QuoteBasicInfo {
        val quotesAmount = entityManager.createNamedQuery("Quote.countAll").singleResult.toString().toLong()
        val randomQuoteId = Random.nextLong(1, quotesAmount)
        return asDto(getQuote(randomQuoteId))
    }

    override fun save(entity: QuoteBasicInfo) {
        Assert.hasText(entity.quote, "Cannot persist quote with no text")
        val newQuote = Quote(entity.quote)
        entityManager.persist(newQuote)
    }

    override fun remove(id: Long) {
        val quote = getQuote(id)
        entityManager.remove(quote)
    }

    override fun update(entity: QuoteBasicInfo) {
        Assert.hasText(entity.quote, "Cannot update quote if no text was provided")
        val quote = getQuote(entity.id)
        quote.quote = entity.quote
        entityManager.merge(quote)
    }

    fun setDisplayDate(entity: QuoteBasicInfo) {
        val quote = getQuote(entity.id)
        quote.displayedDate = LocalDate.now()
        entityManager.merge(quote)
    }
}