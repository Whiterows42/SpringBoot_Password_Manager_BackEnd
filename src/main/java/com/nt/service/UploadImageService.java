package com.nt.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.nt.entity.User;
import com.nt.repo.UserRepo;

@Component
public class UploadImageService {

    @Autowired
    private UserRepo userRepo;

    // Define a directory for storing user profile pictures
    private final String UPLOAD_DIR = "uploads/userpics"; 

    public boolean isUploadUserProfile(int id, MultipartFile file) {
        User user = this.userRepo.findById(id);

        if (user != null) {
            try {
                // Ensure the directory exists
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Get the file's original name
                String fileName = file.getOriginalFilename();
                Path targetLocation = uploadPath.resolve(fileName);

                // Copy the file to the target location (overwriting if it exists)
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                // Update the user's profile picture URL
                user.setProfilePictureUrl(fileName);
                this.userRepo.save(user);

                return true;
            } catch (IOException e) {
                // Log detailed error for debugging
                System.err.println("Error occurred while uploading the file: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                // Catch other unexpected exceptions
                System.err.println("Unexpected error: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("User with ID " + id + " not found.");
        }

        // Return false if any exception occurs or user is not found
        return false;
    }
}
