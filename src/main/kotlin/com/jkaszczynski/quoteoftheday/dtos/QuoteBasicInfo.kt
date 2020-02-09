package com.jkaszczynski.quoteoftheday.dtos

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.validation.constraints.NotEmpty

class QuoteBasicInfo(
        @NotEmpty
        var quote: String
) {
    @JsonIgnore
    var id: Long = 0

    constructor(quote: String, id: Long) : this(quote) {
        this.id = id
    }
}