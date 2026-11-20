package com.barista.infrastructure.database;

import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DatabaseConnectionTest {

    @Autowired
    private JdbcTemplate jdbctemplate;

    @Test
    public void connectsToPostgres() {
        String result = jdbctemplate.queryForObject("SELECT current_database()", String.class);
        assertEquals("barista", result);
    }
}