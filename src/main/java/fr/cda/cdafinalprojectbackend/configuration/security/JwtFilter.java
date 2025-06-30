package fr.cda.cdafinalprojectbackend.configuration.security;

import fr.cda.cdafinalprojectbackend.entity.Jwt;
import fr.cda.cdafinalprojectbackend.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private UserService userService;
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String user = null;
        String token;
        boolean isTokenExpired = true;
        Jwt jwt = null;


        final String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
            isTokenExpired = jwtService.isTokenExpired(token);
            jwt = jwtService.getTokenByValue(token);
            user = jwtService.extractEmail(token);
        }

        if (
                !isTokenExpired
                && jwt.getUser().getEmail().equals(user)
                && SecurityContextHolder.getContext().getAuthentication() == null
        ) {
            UserDetails userDetails = userService.loadUserByUsername(user);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
