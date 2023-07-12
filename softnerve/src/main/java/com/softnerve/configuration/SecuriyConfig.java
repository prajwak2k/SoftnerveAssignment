package com.softnerve.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.softnerve.security.JwtAuthenticationFilter;
import com.softnerve.security.JwtAuthonticationEnterPoint;

@Configuration
@EnableWebSecurity
public class SecuriyConfig  {
	@Autowired
	private JwtAuthonticationEnterPoint enterPoint;
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http 
		.authorizeHttpRequests((requests) -> requests
				. requestMatchers("/api/user/login","")
				.permitAll()
					.anyRequest()
					.authenticated()
					)
		.formLogin((form)-> form
				.loginPage("/login")
				.permitAll()
				.failureUrl("/login?error =true")
				.usernameParameter("username")
				.passwordParameter("password")
				
				)	
		.exceptionHandling((exception)-> exception
				.authenticationEntryPoint(this.enterPoint)
				)
		.sessionManagement((session)-> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
		.csrf(csrf -> csrf.disable());
		
	http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	http.authenticationProvider(daoAuthenticationProvider());
		return http.build();		
		
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		
		return new BCryptPasswordEncoder();
		}
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		return provider;
	}

}
