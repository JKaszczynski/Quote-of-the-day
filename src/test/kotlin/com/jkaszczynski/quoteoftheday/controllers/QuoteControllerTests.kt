package com.jkaszczynski.quoteoftheday.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jkaszczynski.quoteoftheday.cleanDatabase
import com.jkaszczynski.quoteoftheday.dtos.QuoteBasicInfo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@SpringBootTest
@AutoConfigureMockMvc
class QuoteControllerTests(
        @Autowired
        private val mockMvc: MockMvc,
        @Autowired
        private val jdbcTemplate: JdbcTemplate) {


    @BeforeEach
    fun cleanDatabase() {
        cleanDatabase(jdbcTemplate, "Quotes")
    }

    @Test
    fun givenOneQuote_whenGetRequest_thenResponseWithQuote() {
        val quote = "testQuote"
        val author = "testAuthor"
        insertQuote(quote, author)
        mockMvc.perform(get("/"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.quote").value(quote))
                .andExpect(jsonPath("$.author").value(author))
    }

    fun insertQuote(quote: String, author: String) {
        jdbcTemplate.update("INSERT INTO Quotes VALUES(?,?,?,?,?,?)", 1L, author, null, null, null, quote)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun givenAuthenticatedUser_whenPostRequest_thenQuoteCreated() {
        val quote = QuoteBasicInfo("test")
        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(quote)))
                .andExpect(status().isCreated)
    }

    @Test
    fun givenAnonymousUser_whenPostRequest_thenRequestDenied() {
        val quote = QuoteBasicInfo("test")
        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(quote)))
                .andExpect(status().isUnauthorized)
    }
}