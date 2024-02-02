package com.example.HealthArc.Security;

import com.example.HealthArc.Security.UserDetailServiceConfig.DoctorUserDetailService;
import com.example.HealthArc.Security.UserDetailServiceConfig.PatientUserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    final JwtService jwtService;
    @Autowired
    DoctorUserDetailService doctorUserDetailService;
    @Autowired
    PatientUserDetailService patientUserDetailService;
    @Override
    protected void doFilterInternal(
           @NonNull HttpServletRequest request,
           @NonNull HttpServletResponse response,
           @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // header that contains jwt/bearer token
        final String authHeader = request.getHeader("Authorization");

         String jwt = null;
         String userEmail = null;

        if(authHeader != null && authHeader.startsWith("Bearer")){
            jwt = authHeader.substring(7);
            try{
            userEmail = jwtService.extractUsername(jwt);
            }catch (IllegalArgumentException e) {
                logger.info("Illegal Argument while fetching the username !!");
                e.printStackTrace();
            } catch (ExpiredJwtException e) {
                logger.info("Given jwt token is expired !!");
                e.printStackTrace();
            } catch (MalformedJwtException e) {
                logger.info("Some changed has done in token !! Invalid Token");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            logger.info("Invalid Header Value !! ");
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            //fetch user detail from username
            System.out.println("issuer "+jwtService.getUserType(jwt));
            if(jwtService.getUserType(jwt).equals("doctor")){
                 userDetails = this.doctorUserDetailService.loadUserByUsername(userEmail);
            }
            else if (jwtService.getUserType(jwt).equals("patient")) {
                userDetails = this.patientUserDetailService.loadUserByUsername(userEmail);
            }
            if(userDetails != null) {
                boolean validateToken = this.jwtService.isTokenValid(jwt, userDetails);
                if (validateToken) {
                    //set the authentication
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    logger.info("Validation fails !!");
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}