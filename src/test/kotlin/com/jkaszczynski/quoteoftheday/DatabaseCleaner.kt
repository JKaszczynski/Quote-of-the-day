package com.jkaszczynski.quoteoftheday

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.jdbc.JdbcTestUtils

fun cleanDatabase(jdbcTemplate: JdbcTemplate, tableName: String) {
    JdbcTestUtils.deleteFromTables(jdbcTemplate, tableName)
    jdbcTemplate.update("ALTER TABLE Quotes ALTER COLUMN id RESTART WITH 1")
}