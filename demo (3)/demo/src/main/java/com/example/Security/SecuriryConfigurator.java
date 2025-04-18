package com.example.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import com.example.orders.Logging.LoginAttemptAspect;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableWebSecurity
public class SecurityConfigurator {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfigurator.class);
    private static final int MAX_ATTEMPTS = 3;
    private static final ConcurrentHashMap<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    @Autowired
    private LoginAttemptAspect loginAttemptAspect;
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build());
        manager.createUser(User.withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build());
        return manager;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .successHandler((request, response, authentication) -> {
                    logger.info("Successful authentication for user: " + authentication.getName());
                    response.sendRedirect("/home");
                })
                .failureHandler(customFailureHandler())
                .permitAll()
            )
            .logout(logout -> logout
                .permitAll()
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .expiredUrl("/login?expired")
            );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private AuthenticationFailureHandler customFailureHandler() {
    return (HttpServletRequest request, HttpServletResponse response, Exception exception) -> {
        String username = request.getParameter("username");

        if (loginAttemptAspect.isLockedOut(username)) {
            logger.warn("User  " + username + " has been locked out due to too many failed login attempts.");
            response.sendRedirect("/login?error=locked");
        } else {
            logger.warn("Failed login attempt for user: " + username);
            response.sendRedirect("/login?error");
        }
    };
}
}
