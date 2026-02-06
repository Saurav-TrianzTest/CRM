package crm;

import crm.service.SpringDataUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @BeforeEach
    public void setUp() {
        securityConfig = new SecurityConfig();
    }

    @Test
    public void testSecurityConfigConstructor() {
        assertNotNull(securityConfig);
    }

    @Test
    public void testPasswordEncoderNotNull() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
    }

    @Test
    public void testPasswordEncoderType() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testPasswordEncoderEncodes() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String encoded = encoder.encode("password");
        assertNotNull(encoded);
        assertTrue(encoded.length() > 0);
    }

    @Test
    public void testCustomUserDetailsServiceNotNull() {
        SpringDataUserDetailsService service = securityConfig.customUserDetailsService();
        assertNotNull(service);
    }

    @Test
    public void testCustomUserDetailsServiceType() {
        SpringDataUserDetailsService service = securityConfig.customUserDetailsService();
        assertTrue(service instanceof SpringDataUserDetailsService);
    }

    @Test
    public void testAuthenticationProviderNotNull() {
        DaoAuthenticationProvider provider = securityConfig.authenticationProvider();
        assertNotNull(provider);
    }

    @Test
    public void testAuthenticationProviderType() {
        DaoAuthenticationProvider provider = securityConfig.authenticationProvider();
        assertTrue(provider instanceof DaoAuthenticationProvider);
    }

    @Test
    public void testPasswordEncoderDifferentPasswords() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String encoded1 = encoder.encode("password1");
        String encoded2 = encoder.encode("password2");
        assertNotEquals(encoded1, encoded2);
    }

    @Test
    public void testPasswordEncoderSamePasswordDifferentHash() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String encoded1 = encoder.encode("password");
        String encoded2 = encoder.encode("password");
        assertNotEquals(encoded1, encoded2);
    }
}
