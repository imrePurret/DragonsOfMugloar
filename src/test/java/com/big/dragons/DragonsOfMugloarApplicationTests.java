package com.big.dragons;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = DragonsOfMugloarApplication.class)
@ActiveProfiles("test")
class DragonsOfMugloarApplicationTests {

	@Test
	void contextLoads() {
		// Tests that Spring context loads properly
	}
}
