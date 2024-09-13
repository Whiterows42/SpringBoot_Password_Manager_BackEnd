package com.nt.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nt.security.JwtAuthenticationEntryPoint;
import com.nt.security.JwtAuthenticationFilter;


@Configuration
public class JwtConfig {

	@Autowired
	private JwtAuthenticationEntryPoint point;
	@Autowired
	private JwtAuthenticationFilter filter;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	    .cors(cors -> cors
	            .configurationSource(request -> {
	                var corsConfig = new org.springframework.web.cors.CorsConfiguration();
	                corsConfig.setAllowedOrigins(List.of("http://localhost:5173")); // Add allowed origin
	                corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	                corsConfig.setAllowedHeaders(List.of("*"));
	                corsConfig.setExposedHeaders(List.of("Authorization"));
	                corsConfig.setAllowCredentials(true);
	                return corsConfig;
	            }))
	    .csrf(csrf -> csrf.disable())
	            .authorizeHttpRequests(auth -> auth
	                    .requestMatchers("/auth/**").permitAll() // Permit login and create user
	                    .requestMatchers("/userpics/**").permitAll() 
	                    .requestMatchers("/home/**").authenticated() // Restrict other home routes
	                    .anyRequest().authenticated()) // Any other request needs authentication
	            .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	    
	    // Add JWT filter
	    http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
	    
	    return http.build();
	}

	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider  provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		
		return provider;
	}

}
