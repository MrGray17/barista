package com.barista;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
// For this test environment, prepare
// and set up the MockMvc testing infrastructure.
@SpringBootTest
class BaristaApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	// Puts the configured MockMvc in a variable

	@Test
	void contextLoads() {
	}

	@Test 
	void helloEndpoitReturnsResponse () throws Exception {

		mockMvc.perform(get("/hello"))
		.andExpect(status().isOk())
		.andExpect(content().string("Barista is alive")) ;
	}

}
