package org.matei.soa.notification.config;

import org.matei.soa.notification.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsProvider userDetailsProvider)
            throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
//                .cors(httpSecurityCorsConfigurer ->
//                        httpSecurityCorsConfigurer.configurationSource(request ->
//                                new CorsConfiguration().applyPermitDefaultValues()
//                        ))
//                .cors(Customizer.withDefaults())
                .addFilterBefore(new AuthorizationFilter(userDetailsProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.GET, "/")
                                .permitAll()
//                        .requestMatchers(HttpMethod.POST, "/user/new", "/user/login")
                                .requestMatchers(HttpMethod.POST, "/user/new", "/user/login", "/ws/notifications", "/notifications/**")
                                .permitAll()
                                .requestMatchers("/user/adminRole/**")
                                .hasRole(Role.ADMIN.name())
                                .requestMatchers("/**")
                                .permitAll()
//                        .hasAnyRole(Role.USER.name(), Role.ADMIN.name()   )
                )
                .build();
    }

    @Bean
    public WebClient userServiceWebClient(@Value("${soa.project.properties.users.url}") String usersServiceUrl) {
        return WebClient.builder().baseUrl(usersServiceUrl).build();
    }

}
