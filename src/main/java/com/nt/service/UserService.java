package com.nt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nt.entity.User;
import com.nt.repo.UserRepo;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public String createUser(User u) {
	    // Retrieve user by email
	    Optional<User> existingUser = this.userRepo.findByEmail(u.getEmail());

	    // Check if user already exists
	    if (existingUser.isPresent()) {
	        return "Email Already Exists";
	    }

	    // Encode the password
	    u.setPassword(passwordEncoder.encode(u.getPassword()));

	    // Save the new user
	    User user = this.userRepo.save(u);

	    // Check if user is saved successfully
	    if (user != null) {
	        return "User Created Successfully";
	    }

	    return "Failed to Create User";
	}


	public boolean verifyUserEntity(String email, String password) {
	    List<User> users = this.userRepo.findByEmailAndPassword(email, passwordEncoder.encode(password));
	    
	  
	    return !users.isEmpty();
	}

	public String verifyUserEmail(String email) {
		
		Optional<User> optionalUsers = this.userRepo.findByEmail(email);

		if (optionalUsers.isPresent()) {
		  
		    
		            return email;
		    }
		

		// If no user is found or the list is empty
		return null; // or handle accordingly

	}
	
	public Optional<User> showUserByEmail(String email) {
	    // Retrieve user from the repository by email
	    Optional<User> optionalUser = this.userRepo.findByEmail(email);
	    
	    // Check if the user is present
	    if (optionalUser.isPresent()) {
	        User user = optionalUser.get();
	        String filename = user.getProfilePictureUrl();
	        
	        if (filename != null && !filename.isEmpty()) {
	            // Construct the full image URL
	            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
	                .path("/userpics/") // Path to the user profile pictures
	                .path(filename)
	                .toUriString();
	            
	            // Set the full image URL back to the user object
	            user.setProfilePictureUrl(imageUrl);
	        }
	        
	        return Optional.of(user); // Return the updated user
	    }
	    
	    // Return an empty Optional if no user found
	    return Optional.empty();
	}


	
	public boolean updateUser(User u, int id) {
		
	User user =	this.userRepo.findById(id);
		if (user != null) {
			u.setId(id);
			u.setProfilePictureUrl(user.getProfilePictureUrl());
			this.userRepo.save(u);
			
			return true;
		}
		
	return	false;
	}


}
