package com.lcwd.electronic.store.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Authorization
        String requestHeader = request.getHeader("Authorization");
        //starts with : Bearer 23jdkw24334k5k3k3k3k
        log.info("Header :{}", requestHeader);
        String username = null;
        String token = null;

        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            token = requestHeader.substring(7);
            try {
                log.info("Fetching username from token");
                username = this.jwtTokenHelper.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                log.info("Unable to get Jwt token !! IllegalArgument occurred while fetching username");
                e.printStackTrace();
            } catch (ExpiredJwtException e) {
                log.info("Given jwt token has expired !!");
                e.printStackTrace();
            } catch (MalformedJwtException e) {
                log.info("Some changes has done in token, Invalid token !!");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log.info("Jwt token does not begin with Bearer");
        }

        //Once we get token we have to validate it
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (this.jwtTokenHelper.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.info("Validation failed !!");
            }
        } else {
            log.info("Username is null or context is not null !!");
        }
        filterChain.doFilter(request, response);
    }
}
