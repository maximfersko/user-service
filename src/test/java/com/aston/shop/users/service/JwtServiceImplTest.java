package com.aston.shop.users.service;

import com.aston.shop.users.service.impl.JwtServiceImpl;
import com.aston.shop.users.utils.DataUtils;
import org.junit.jupiter.api.DisplayName;
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
		"jwt.token.secret=w345edfrtgy67y7654ersdxcfngmh,ju8y7y6gtfhcvb",
		"jwt.token.lifetime=3600000" // 1 hour
})
class JwtServiceImplTest {

	@MockBean
	private JwtServiceImpl jwtService;

	@Mock
	private UserDetails userDetails;

	@Test
	@DisplayName("Test Extract Username from JWT Token")
	void givenJwtToken_whenExtractUsername_thenEqualsExtractedUsername() {
		//given
		String token = DataUtils.JWT_TOKEN_FOR_TEST;

		//when
		when(jwtService.extractUserName(token)).thenReturn("username");
		String extractedUsername = jwtService.extractUserName(token);

		//then
		assertEquals("username", extractedUsername);
	}


	@Test
	@DisplayName("Test Validate JWT Token")
	void givenJwtToken_whenIsTokenValid_thenAssertValidateToken() {
		//given
		String token = DataUtils.JWT_TOKEN_FOR_TEST;

		//when
		when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);
		boolean isValid = jwtService.isTokenValid(token, userDetails);

		//then
		assertTrue(isValid);
	}
}
