package com.hutech.CAOXUANBINH.config;

import com.hutech.CAOXUANBINH.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final UserService userService;
        private final PasswordEncoder passwordEncoder;

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
                var auth = new DaoAuthenticationProvider();
                auth.setUserDetailsService(userService);
                auth.setPasswordEncoder(passwordEncoder);
                return auth;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                                .authorizeHttpRequests(auth -> auth
                                                // Trang doi diem/voucher chi cho user
                                                .requestMatchers(antMatcher("/rewards/**")).hasAuthority("ROLE_USER")
                                                // API tra cuu diem chi cho user
                                                .requestMatchers(antMatcher("/order/points")).hasAuthority("ROLE_USER")
                                                // User duoc tra cuu lich su don hang
                                                .requestMatchers(antMatcher(HttpMethod.GET, "/order/history"))
                                                .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                                                // User duoc vao trang tra cuu/list san pham admin
                                                .requestMatchers(antMatcher(HttpMethod.GET, "/admin/products"))
                                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
                                                // Manager/Admin duoc quan ly product
                                                .requestMatchers(
                                                                antMatcher("/products/add"),
                                                                antMatcher("/products/edit/**"),
                                                                antMatcher("/products/update/**"),
                                                                antMatcher("/products/delete/**"))
                                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
                                                // Danh muc chi admin
                                                .requestMatchers(
                                                                antMatcher("/admin/categories"),
                                                                antMatcher("/admin/categories/**"))
                                                .hasAuthority("ROLE_ADMIN")
                                                // Chan manager vao cac phan con lai
                                                .requestMatchers(
                                                                antMatcher("/cart"),
                                                                antMatcher("/cart/**"),
                                                                antMatcher("/order"),
                                                                antMatcher("/order/**"))
                                                .access(new WebExpressionAuthorizationManager(
                                                                "!hasAuthority('ROLE_MANAGER')"))
                                                // Public routes
                                                .requestMatchers(
                                                                antMatcher("/css/**"),
                                                                antMatcher("/js/**"),
                                                                antMatcher("/images/**"),
                                                                antMatcher("/fonts/**"),
                                                                antMatcher("/"),
                                                                antMatcher("/home"),
                                                                antMatcher("/products"),
                                                                antMatcher("/products/detail/**"),
                                                                antMatcher("/cart"),
                                                                antMatcher("/cart/**"),
                                                                antMatcher("/order/**"),
                                                                antMatcher("/login"),
                                                                antMatcher("/register"),
                                                                antMatcher("/error"),
                                                                antMatcher("/403"),
                                                                antMatcher("/api/**"))
                                                .permitAll()
                                                // Admin routes
                                                .requestMatchers(antMatcher("/admin/**")).hasAuthority("ROLE_ADMIN")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/", true)
                                                .failureUrl("/login?error=true")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout=true")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())
                                .exceptionHandling(exception -> exception
                                                .accessDeniedPage("/403"))
                                .authenticationProvider(authenticationProvider())
                                .build();
        }
}
