package com.softnerve.security;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	

	@Autowired
	JwtToken jwtToken;
	
	@Autowired
	UserDetailsService detailsService;
	

	@Bean
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		String requestToken = request.getHeader("Authorizatio");
		System.out.println(requestToken);
		String username = null;
		String token = null;
		
		if(requestToken!= null && requestToken.startsWith("Bearer")) {
			token = requestToken.substring(7);
			try {
				username = this.jwtToken.getUsernameFromToken(token);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get token ");
			}catch(ExpiredJwtException e){
				System.out.println("Token Expired");
			}catch (MalformedJwtException e) {
				System.out.println("invaild jwt Token");
			}
		}else {
			System.out.println(" Token does not start with Bearer");
			
		}
		
		// now validate 
		if (username != null && SecurityContextHolder.getContext().getAuthentication()== null) {
			UserDetails details = this.detailsService.loadUserByUsername(username);
			if(this.jwtToken.validateToken(token, details)){
				UsernamePasswordAuthenticationToken  authenticationToken = new UsernamePasswordAuthenticationToken(details,null,details.getAuthorities());
				
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				
			}else {
				
				System.out.println("invalid Jwt Token");
			}
		} else {
			System.out.println("username is null and Context is null");

		}
		filterChain.doFilter(request, response);	
		
	}

}
