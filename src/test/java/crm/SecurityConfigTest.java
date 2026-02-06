package crm;

import crm.service.SpringDataUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
    }

    @Test
    void testSecurityConfigCreation() {
        assertNotNull(securityConfig);
    }

    @Test
    void testPasswordEncoder() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void testPasswordEncoderEncodesPassword() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String rawPassword = "testPassword";
        String encodedPassword = encoder.encode(rawPassword);

        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void testPasswordEncoderWithDifferentPasswords() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String password1 = "password1";
        String password2 = "password2";

        String encoded1 = encoder.encode(password1);
        String encoded2 = encoder.encode(password2);

        assertNotEquals(encoded1, encoded2);
    }

    @Test
    void testCustomUserDetailsService() {
        SpringDataUserDetailsService service = securityConfig.customUserDetailsService();
        assertNotNull(service);
        assertTrue(service instanceof SpringDataUserDetailsService);
    }

    @Test
    void testPasswordEncoderWithEmptyPassword() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String emptyPassword = "";
        String encodedPassword = encoder.encode(emptyPassword);

        assertNotNull(encodedPassword);
        assertTrue(encoder.matches(emptyPassword, encodedPassword));
    }

    @Test
    void testPasswordEncoderWithNullPassword() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();

        assertThrows(IllegalArgumentException.class, () -> {
            encoder.encode(null);
        });
    }

    @Test
    void testPasswordEncoderConsistency() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String rawPassword = "testPassword";
        String encodedPassword = encoder.encode(rawPassword);

        assertTrue(encoder.matches(rawPassword, encodedPassword));
        assertFalse(encoder.matches("wrongPassword", encodedPassword));
    }
}
