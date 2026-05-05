package crm;

import crm.service.SpringDataUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
    void passwordEncoder_shouldReturnBCryptPasswordEncoder() {
        // Act
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        
        // Assert
        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void customUserDetailsService_shouldReturnSpringDataUserDetailsService() {
        // Act
        SpringDataUserDetailsService service = securityConfig.customUserDetailsService();
        
        // Assert
        assertNotNull(service);
        assertTrue(service instanceof SpringDataUserDetailsService);
    }

    @Test
    void authenticationProvider_shouldReturnDaoAuthenticationProvider() {
        // Act
        DaoAuthenticationProvider provider = securityConfig.authenticationProvider();
        
        // Assert
        assertNotNull(provider);
        assertTrue(provider instanceof DaoAuthenticationProvider);
    }

    @Test
    void passwordEncoder_shouldEncodePassword() {
        // Arrange
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String rawPassword = "testPassword123";
        
        // Act
        String encoded = encoder.encode(rawPassword);
        
        // Assert
        assertNotNull(encoded);
        assertNotEquals(rawPassword, encoded);
        assertTrue(encoder.matches(rawPassword, encoded));
    }

    @Test
    void passwordEncoder_shouldProduceDifferentHashesForSamePassword() {
        // Arrange
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String password = "testPassword";
        
        // Act
        String hash1 = encoder.encode(password);
        String hash2 = encoder.encode(password);
        
        // Assert
        assertNotEquals(hash1, hash2);
        assertTrue(encoder.matches(password, hash1));
        assertTrue(encoder.matches(password, hash2));
    }
}
