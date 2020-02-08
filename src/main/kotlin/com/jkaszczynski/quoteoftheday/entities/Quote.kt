package com.jkaszczynski.quoteoftheday.entities

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "Quotes")
@NamedQueries(
        NamedQuery(name = "Quote.getByDisplayDate",
                query = "SELECT new com.jkaszczynski.quoteoftheday.dtos.QuoteBasicInfo(q.quote) FROM Quotes q WHERE q.displayedDate = :date")
)
data class Quote(
        @Column(nullable = false)
        var quote: String = "") {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1

    @CreatedDate
    lateinit var createdDate: LocalDateTime

    @LastModifiedDate
    lateinit var modifiedDate: LocalDateTime

    lateinit var displayedDate: LocalDate
}