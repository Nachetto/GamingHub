package dev.nacho.ghub.common.security;

import dev.nacho.ghub.common.Constants;
import dev.nacho.ghub.filters.TokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final TokenFilter jwtAuthFilter;
    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(AuthenticationProvider authenticationProvider, TokenFilter jwtAuthFilter, CustomOAuth2UserService customOAuth2UserService) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthFilter = jwtAuthFilter;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)

                .formLogin(form -> form
                        .loginPage("/api/auth/loginPage")
                        .permitAll()
                )

                .authorizeHttpRequests(req -> req
                                .requestMatchers(
                                        "/api/auth/**",
                                        "/error",
                                        "/auth/register",
                                        Constants.BASE_URL+ Constants.AUTH_URL+Constants.PATH_LOGIN,
                                        Constants.BASE_URL+ Constants.AUTH_URL+Constants.PATH_REGISTER,
                                        "/",
                                        "/loginPage",
                                        "/favicon.ico",
                                        "/oauth2/**",
                                        "/login/**",
                                        "/api/auth/send-code",
                                        "/api/auth/validate-code")
                                .permitAll().anyRequest().authenticated()
                )

                .exceptionHandling(configurer ->
                        configurer.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )

                // Configuración de OAuth2 login
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/google")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )

                .authenticationProvider(authenticationProvider)

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}
