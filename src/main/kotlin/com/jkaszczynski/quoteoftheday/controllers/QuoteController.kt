package com.jkaszczynski.quoteoftheday.controllers

import com.jkaszczynski.quoteoftheday.dtos.QuoteBasicInfo
import com.jkaszczynski.quoteoftheday.services.QuoteService
import com.jkaszczynski.quoteoftheday.services.daos.quote.QuoteDao
import org.springframework.context.annotation.Role
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class QuoteController(
        private val quoteService: QuoteService,
        private val quoteDao: QuoteDao) {

    @GetMapping
    fun getQuote(): String {
        return quoteService.getTodayQuote()
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun <T : Any> saveQuote(@RequestBody quote: QuoteBasicInfo): ResponseEntity<T> {
        quoteDao.save(QuoteBasicInfo(quote.quote))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}