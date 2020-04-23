package com.jkaszczynski.quoteoftheday.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class BasicAuthenticationController {
    @GetMapping
    fun <T : Any> authenticate(): ResponseEntity<T> {
        return ResponseEntity.status(HttpStatus.OK).build()
    }
}