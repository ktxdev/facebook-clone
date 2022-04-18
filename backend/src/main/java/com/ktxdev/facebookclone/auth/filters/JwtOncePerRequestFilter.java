package com.ktxdev.facebookclone.auth.filters;

import com.auth0.jwt.JWT;
import com.ktxdev.facebookclone.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
public class JwtOncePerRequestFilter extends OncePerRequestFilter {

    private static final String BEARER_TOKEN_START_TEXT = "Bearer ";

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        String username = null;
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (nonNull(token) && token.startsWith(BEARER_TOKEN_START_TEXT)) {
            token = token.substring(BEARER_TOKEN_START_TEXT.length());
            username = JWT.decode(token).getSubject();
        }

        val authentication = SecurityContextHolder.getContext().getAuthentication();
        if (nonNull(username) && isNull(authentication)) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            val usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        chain.doFilter(request, response);
    }
}
