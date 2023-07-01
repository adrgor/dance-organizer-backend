package org.example.event.security;

import com.auth0.jwt.JWT;
import lombok.AllArgsConstructor;
import org.example.event.clients.UserClient;
import org.example.event.model.AuthenticatedUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    UserClient userClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        boolean isUserValidResponse = userClient.validateToken(jwt);

        if(isUserValidResponse) {
            AuthenticatedUser authenticatedUser = new AuthenticatedUser(JWT.decode(jwt).getClaim("id").asInt());
            Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, "", null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}