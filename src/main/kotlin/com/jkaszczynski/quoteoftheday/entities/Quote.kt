package com.jkaszczynski.quoteoftheday.entities

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "Quotes")
@NamedQueries(
        NamedQuery(name = "Quote.getByDisplayDate",
                query = "SELECT new com.jkaszczynski.quoteoftheday.dtos.QuoteBasicInfo(q.quote, q.author, q.id) FROM Quotes q WHERE q.displayedDate = :date"),
        NamedQuery(name = "Quote.getNotDisplayed",
                query = "SELECT new com.jkaszczynski.quoteoftheday.dtos.QuoteBasicInfo(q.quote, q.author, q.id) FROM Quotes q WHERE q.displayedDate IS NULL"),
        NamedQuery(name = "Quote.countAll",
                query = "SELECT COUNT(q) FROM Quotes q")
)
data class Quote(
        @Column(nullable = false)
        var quote: String = "",
        var author: String = ANONYMOUS_AUTHOR) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1

    @CreatedDate
    lateinit var createdDate: LocalDateTime

    @LastModifiedDate
    lateinit var modifiedDate: LocalDateTime

    lateinit var displayedDate: LocalDate

    companion object {
        const val ANONYMOUS_AUTHOR = "Anonymous"
    }
}