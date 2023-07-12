package com.softnerve.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softnerve.security.JwtRequest;
import com.softnerve.security.JwtResponse;
import com.softnerve.security.JwtToken;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	private JwtToken jwtToken;
	@Autowired
	private UserDetailsService service;
	@Autowired
	private DaoAuthenticationProvider provider;
	@PostMapping("/login")
	@ApiOperation(value = "login Patient")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400 , message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error")
			})
	public ResponseEntity<JwtResponse> createToken(@RequestBody JwtRequest request){
		this.authenticate(request.getUsername(),request.getPassword());
		UserDetails user =this.service.loadUserByUsername(request.getUsername());
	String token =	this.jwtToken.generateToken(user);
		JwtResponse response = new JwtResponse();
		response.setToken(token);
		return new ResponseEntity<JwtResponse>(response,HttpStatus.OK);	
	}
	private void authenticate(String username, String password) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
		this.provider.authenticate(token);
	}
}
