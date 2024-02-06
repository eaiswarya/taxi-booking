package com.example.taxibooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.example.taxibooking.model.User;
import com.example.taxibooking.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
public class jwtServiceTest {

    @InjectMocks private JwtService jwtService;

    @Mock private User user;

    @Mock private UserDetails userDetails;

    private String token;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(
                jwtService,
                "secretKey",
                "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437");
        ReflectionTestUtils.setField(jwtService, "expirationTime", 3600000);

        when(user.getId()).thenReturn(1L);
        when(user.getName()).thenReturn("ais");
        when(user.getEmail()).thenReturn("ais@gmail.com");

        token = jwtService.generateToken(user);
    }

    @Test
    public void testGenerateToken() {
        assertNotNull(token);
        assertEquals(user.getEmail(), jwtService.extractEmail(token));
    }

    @Test
    public void testExtractEmail() {
        assertEquals(user.getEmail(), jwtService.extractEmail(token));
    }

    @Test
    public void testIsTokenExpired() {
        assertFalse(jwtService.isTokenExpired(token));
    }
}
