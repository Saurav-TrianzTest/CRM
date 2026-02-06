package crm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest_New {

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
    }

    @Test
    void testPasswordEncoder() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
    }

    @Test
    void testPasswordEncoderEncodes() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String encoded = encoder.encode("password");
        assertNotNull(encoded);
        assertNotEquals("password", encoded);
    }

    @Test
    void testCustomUserDetailsService() {
        assertNotNull(securityConfig.customUserDetailsService());
    }

    @Test
    void testSecurityConfigNotNull() {
        assertNotNull(securityConfig);
    }
}
