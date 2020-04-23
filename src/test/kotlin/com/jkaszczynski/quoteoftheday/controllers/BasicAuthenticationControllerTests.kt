package com.jkaszczynski.quoteoftheday.controllers

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class BasicAuthenticationControllerTests(
        @Autowired
        private val mockMvc: MockMvc) {

    @Test
    fun givenUser_whenRequest_thenAccessDenied() {
        mockMvc.perform(get("/auth"))
                .andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun givenAdmin_whenRequest_thenStatusOk() {
        mockMvc.perform(get("/auth"))
                .andExpect(status().isOk)
    }
}