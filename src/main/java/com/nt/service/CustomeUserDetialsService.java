package com.nt.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nt.entity.User;
import com.nt.repo.UserRepo;

@Service
public class CustomeUserDetialsService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		// load user from database
		User user =  userRepo.findByEmail(username).orElseThrow(()-> new RuntimeException("user not found"));
		
		return user;
	}

}
