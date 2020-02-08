package com.jkaszczynski.quoteoftheday.dtos.dtos

import javax.validation.constraints.NotEmpty

class QuoteDto(
        @NotEmpty
        var quote: String
) {
    var id: Long = 0
}