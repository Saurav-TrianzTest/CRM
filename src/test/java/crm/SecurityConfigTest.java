package crm;

import crm.service.SpringDataUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @InjectMocks
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
    }

    @Test
    void testPasswordEncoder() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();

        assertNotNull(encoder);

        String rawPassword = "testPassword123";
        String encodedPassword = encoder.encode(rawPassword);

        assertNotNull(encodedPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
        assertFalse(encoder.matches("wrongPassword", encodedPassword));
    }

    @Test
    void testCustomUserDetailsService() {
        SpringDataUserDetailsService userDetailsService = securityConfig.customUserDetailsService();

        assertNotNull(userDetailsService);
        assertInstanceOf(SpringDataUserDetailsService.class, userDetailsService);
    }

    @Test
    void testAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = securityConfig.authenticationProvider();

        assertNotNull(authProvider);
        assertInstanceOf(DaoAuthenticationProvider.class, authProvider);
    }


    @Test
    void testFilterChainNotNull() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.formLogin(any())).thenReturn(http);
        when(http.logout(any())).thenReturn(http);
        when(http.exceptionHandling(any())).thenReturn(http);
        when(http.build()).thenReturn(mock(DefaultSecurityFilterChain.class));

        SecurityFilterChain filterChain = securityConfig.filterChain(http);

        assertNotNull(filterChain);
    }

    @Test
    void testPasswordEncoderStrength() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();

        String password = "MySecurePassword123!";
        String encoded1 = encoder.encode(password);
        String encoded2 = encoder.encode(password);

        // BCrypt should generate different hashes for the same password
        assertNotEquals(encoded1, encoded2);

        // But both should match the original password
        assertTrue(encoder.matches(password, encoded1));
        assertTrue(encoder.matches(password, encoded2));
    }

    @Test
    void testPasswordEncoderWithEmptyString() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();

        String emptyPassword = "";
        String encoded = encoder.encode(emptyPassword);

        assertNotNull(encoded);
        assertTrue(encoder.matches(emptyPassword, encoded));
    }

    @Test
    void testPasswordEncoderWithSpecialCharacters() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();

        String complexPassword = "P@ssw0rd!#$%^&*()_+-=[]{}|;:',.<>?/~`";
        String encoded = encoder.encode(complexPassword);

        assertTrue(encoder.matches(complexPassword, encoded));
    }
}
