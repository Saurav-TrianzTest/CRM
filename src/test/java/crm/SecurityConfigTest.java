package crm;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    private SecurityConfig securityConfig = new SecurityConfig();

    @Test
    void testPasswordEncoder() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
    }

    @Test
    void testPasswordEncoderEncodesPassword() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String rawPassword = "password123";
        String encoded = encoder.encode(rawPassword);

        assertNotNull(encoded);
        assertNotEquals(rawPassword, encoded);
        assertTrue(encoder.matches(rawPassword, encoded));
    }

    @Test
    void testCustomUserDetailsService() {
        assertNotNull(securityConfig.customUserDetailsService());
    }

    @Test
    void testAuthenticationProvider() {
        assertNotNull(securityConfig.authenticationProvider());
    }

    @Test
    void testSecurityConfigInstantiation() {
        assertNotNull(securityConfig);
    }
}
