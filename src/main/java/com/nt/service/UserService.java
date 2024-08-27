package com.nt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nt.entity.User;
import com.nt.repo.UserRepo;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;

	public String saveUser(User u) {

		List<User> users = this.userRepo.findByEmail(u.getEmail());

		if (!users.isEmpty()) {
			return "Email Already Exist";
		}
		User user = this.userRepo.save(u);
		if (user != null) {

			return "created";
		}
		return "faild to create user";
	}

	public boolean verifyUserEntity(String email, String password) {
	    List<User> users = this.userRepo.findByEmailAndPassword(email, password);
	    
	  
	    return !users.isEmpty();
	}

	public String verifyUserEmail(String email) {
		
	 List<User> users =	this.userRepo.findByEmail(email);
	 for(User list: users) {
		 if (list.getEmail().equals(email)) {
			 
			 
			 
			return email;
		}
	 }
	 return null;
	}
	
	public List<User> showUsersList(String email) {
	    List<User> users = this.userRepo.findByEmail(email);
	    
	    if (users != null) {
	        for (User user : users) {
	            String filename = user.getProfilePictureUrl();
	            if (filename != null && !filename.isEmpty()) {
	                // Construct the full image URL
	                String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
	                    .path("/userpics/") // Your folder path
	                    .path(filename)
	                    .toUriString();
	                
	                user.setProfilePictureUrl(imageUrl); // Set the full image URL
	            }
	        }
	        return users;
	    }
	    
	    return null;
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
