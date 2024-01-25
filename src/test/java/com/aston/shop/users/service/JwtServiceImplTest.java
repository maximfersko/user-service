package com.aston.shop.users.service;

import com.aston.shop.users.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(properties = {
		"jwt.token.secret=mySecretKey",
		"jwt.token.lifetime=3600000" // 1 hour
})
class JwtServiceImplTest {

	@MockBean
	private JwtServiceImpl jwtService;

	@Mock
	private UserDetails userDetails;

	@Test
	void testExtractUserName() {
		String token = "your_generated_token_here";

		when(jwtService.extractUserName(token)).thenReturn("username");

		String extractedUsername = jwtService.extractUserName(token);

		assertEquals("username", extractedUsername);
	}


	@Test
	void testIsTokenValid() {
		String token = "your_generated_token_here";

		when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);

		boolean isValid = jwtService.isTokenValid(token, userDetails);

		assertTrue(isValid);
	}
}
