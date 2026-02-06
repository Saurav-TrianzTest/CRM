package crm;

import crm.service.SpringDataUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringDataUserDetailsService customUserDetailsService() {
        return new SpringDataUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/user/delete/**")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/pdf-generator")).hasAnyRole("ADMIN", "USER", "MANAGER", "OWNER")
                .requestMatchers(new AntPathRequestMatcher("/search/**")).hasAnyRole("ADMIN", "USER", "MANAGER", "OWNER")
                .requestMatchers(new AntPathRequestMatcher("/customer/**")).hasAnyRole("ADMIN", "USER", "MANAGER", "OWNER")
                .requestMatchers(new AntPathRequestMatcher("/user/edit/**")).hasAnyRole("ADMIN", "USER", "MANAGER", "OWNER")
                .requestMatchers(new AntPathRequestMatcher("/user/list")).hasAnyRole("ADMIN", "USER", "MANAGER", "OWNER")
                .requestMatchers(new AntPathRequestMatcher("/contract/**")).hasAnyRole("ADMIN", "USER", "MANAGER", "OWNER")
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/403")
            );
        return http.build();
    }

}
