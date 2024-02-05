package com.matei.users.config;

import javax.net.ssl.SSLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.reactive.function.client.WebClient;

import com.matei.users.jwt.JwtService;
import com.matei.users.model.Role;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import reactor.netty.http.client.HttpClient;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsProvider userDetailsProvider,
            JwtService jwtService) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
//                .cors(httpSecurityCorsConfigurer ->
//                        httpSecurityCorsConfigurer.configurationSource(request ->
//                                new CorsConfiguration().applyPermitDefaultValues()
//                        ))
//                .cors(Customizer.withDefaults())
                .addFilterBefore(new AuthorizationFilter(userDetailsProvider, jwtService),
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.GET, "/").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/user/new", "/user/login")
                        .requestMatchers(HttpMethod.POST, "/user/new", "/user/login", "/ws/notifications",
                                "/notifications/**")
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
    @Profile("jwtServiceFaaS")
    public WebClient jwtFaasWebClient(@Value("${soa.project.properties.azure.faas}") String faasUrl)
            throws SSLException {
        SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

        return WebClient.builder().baseUrl(faasUrl).clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }
}
