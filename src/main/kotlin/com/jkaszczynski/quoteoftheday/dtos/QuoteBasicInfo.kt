package com.jkaszczynski.quoteoftheday.dtos

import javax.validation.constraints.NotEmpty

class QuoteBasicInfo(
        @NotEmpty
        var quote: String
) {
    var id: Long = 0

    constructor(quote: String, id: Long) : this(quote) {
        this.id = id
    }
}