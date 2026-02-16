package com.arquitechthor.kopi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.arquitechthor.kopi.config.TestSecurityConfig;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestSecurityConfig.class)
class KopiToolsApplicationTests {

	@Test
	void contextLoads() {
	}

}
