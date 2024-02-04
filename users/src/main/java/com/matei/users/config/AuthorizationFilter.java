package com.matei.users.config;

import com.matei.users.jwt.JwtService;
import com.matei.users.service.TextBoardException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final static Set<String> pathsWithoutAuth = Set.of("", "user/login", "user/new");
    private final UserDetailsProvider userDetailsProvider;
    private final JwtService jwtService;

    private boolean authorizationRequired(HttpServletRequest request) {
        var path = request.getServletPath();
        path = StringUtils.strip(path, "/");
        return !pathsWithoutAuth.contains(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (authorizationRequired(request)) {
                var userName = jwtService.extractUsername(request.getHeader(HttpHeaders.AUTHORIZATION));
                var userDetails = userDetailsProvider.getUserDetails(userName);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userName, null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + userDetails.getRole())));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(request, response);
        } catch (JwtException | TextBoardException ex) {
            log.error("Error during authorization filter", ex);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
        }
    }
}
