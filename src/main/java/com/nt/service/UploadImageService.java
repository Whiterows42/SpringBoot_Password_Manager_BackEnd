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

    private final String UPLOAD_DIR = "uploads/userpics"; 

    public boolean isUploadUserProfile(int id, MultipartFile file) {
        User user = this.userRepo.findById(id);

        if (user != null) {
            try {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = file.getOriginalFilename();
                Path targetLocation = uploadPath.resolve(fileName);

                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                user.setProfilePictureUrl(fileName);
                this.userRepo.save(user);

                return true;
            } catch (IOException e) {
                System.err.println("Error occurred while uploading the file: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Unexpected error: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("User with ID " + id + " not found.");
        }

        return false;
    }
}
