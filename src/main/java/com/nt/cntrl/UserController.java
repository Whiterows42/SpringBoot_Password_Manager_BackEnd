package com.nt.cntrl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nt.entity.User;
import com.nt.service.UploadImageService;
import com.nt.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private UploadImageService uploadImageService;

	@PostMapping("/create_user")
	public ResponseEntity<String> createUserEntity(@RequestBody User entity) {
		// TODO: process POST request
		try {
			String response = this.userService.saveUser(entity);
			if (response.equals("Email Already Exist")) {
				return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(response);
			} else if (response.equals("created")) {
				return ResponseEntity.ok().body(response);
			}

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("something went wrong");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");

		}
	}

	@PostMapping("/login_user/{email}/{password}")
	public ResponseEntity<String> loginUserEntity(@PathVariable String email, @PathVariable String password) {
		// TODO: process POST request
//		System.out.println(email);
//		System.out.println(password);
		System.out.println("ajits");

		try {

			boolean isLoged = this.userService.verifyUserEntity(email, password);
			if (isLoged) {
				return ResponseEntity.ok("Login Successfully");
			}

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not found");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong");
		}

	}

	@GetMapping("/show_user_details/{email}")
	public ResponseEntity<List<User>> showUserDetailsEnity(@PathVariable String email) {
		List<User> users = this.userService.showUsersList(email);
		try {
			if (users != null) {
				return ResponseEntity.ok().body(users);
			}

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(users);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(users);
		}

	}

	@PostMapping("/upload_profile/pic/{id}")
	public ResponseEntity<String> uploadUserProfilePic(@PathVariable int id,  MultipartFile file) {
	    // Define allowed file extensions and size limit
	    final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");
	    final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB

	    if (file.isEmpty()) {
	        return ResponseEntity.badRequest().body("No file uploaded");
	    }

	    // Check file size
	    if (file.getSize() > MAX_FILE_SIZE) {
	        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File size exceeds the maximum limit of 10 MB");
	    }

	    // Check file extension
	    String fileExtension = getFileExtension(file.getOriginalFilename());
	    if (!ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
	        return ResponseEntity.badRequest().body("Invalid file type. Only JPG, JPEG, and PNG are allowed");
	    }

	    try {
	        boolean isUploaded = this.uploadImageService.isUploadUserProfile(id, file);
	        if (isUploaded) {
	            return ResponseEntity.ok("File uploaded successfully");
	        } else {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while uploading the file");
	    }
	}

	private String getFileExtension(String filename) {
	    int lastDotIndex = filename.lastIndexOf('.');
	    if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
	        return filename.substring(lastDotIndex + 1);
	    }
	    return "";
	}
	
	
	@PutMapping("update_user/{id}")
	public ResponseEntity<String> putMethodName(@PathVariable int id, @RequestBody User entity) {
		try {
			boolean isupdate =	this.userService.updateUser(entity, id);
			if (isupdate) {
				
				return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found ");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("some thing went wrong");
		}
	
	}

}
