package com.jkaszczynski.quoteoftheday.dtos

import com.fasterxml.jackson.annotation.JsonIgnore
import com.jkaszczynski.quoteoftheday.entities.Quote
import javax.validation.constraints.NotEmpty

class QuoteBasicInfo(
        @NotEmpty
        var quote: String
) {
    @JsonIgnore
    var id: Long = 0

    var author: String = Quote.ANONYMOUS_AUTHOR

    constructor(quote: String, author: String, id: Long) : this(quote) {
        this.id = id
        this.author = author
    }
}