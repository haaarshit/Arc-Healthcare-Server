//package com.example.HealthArc.Security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthFilter extends OncePerRequestFilter {
//
//    final JwtService jwtService;
//
//    @Override
//    protected void doFilterInternal(
//           @NonNull HttpServletRequest request,
//           @NonNull HttpServletResponse response,
//           @NonNull FilterChain filterChain
//    ) throws ServletException, IOException {
//        // header that contains jwt/bearer token
//        final String authHeader = request.getHeader("Authorization");
//        final String jwt;
//        final String userEmail;
//
//        if(authHeader == null || !authHeader.startsWith("Bearer ")){
//            filterChain.doFilter(request,response);
//            return;
//        }
//
//        jwt = authHeader.substring(7);
//
//        // call jwt service to extract username/email
//        userEmail = jwtService.extractUsername(jwt);
//
//    }
//}