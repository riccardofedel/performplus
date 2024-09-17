package com.finconsgroup.performplus.manager.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.finconsgroup.performplus.manager.security.CustomUsernamePasswordAuthenticationToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter { 


	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
               String authToken = request.getHeader(this.tokenHeader);
        UserDetails userDetails = null;
        if(authToken != null){
            userDetails = jwtTokenUtil.getUserDetails(jwtTokenUtil.normalize(authToken));
        }
        if (userDetails != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Ricostruisco l userdetails con i dati contenuti nel token
            // controllo integrita' token
            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                CustomUsernamePasswordAuthenticationToken authentication = new CustomUsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}