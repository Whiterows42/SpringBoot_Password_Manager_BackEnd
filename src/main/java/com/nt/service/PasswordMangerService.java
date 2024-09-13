package com.nt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nt.entity.PasswordManager;
import com.nt.repo.PasswordMangerRepo;

@Service
public class PasswordMangerService {

    @Autowired
    private PasswordMangerRepo passRepo;

    @Autowired
    private EncryptionService encryptionService;

    // Store new password with encryption
    public boolean storeNewPassword(PasswordManager pass) {
        boolean isAdd = false;
        if (pass != null && pass.getPassword() != null) {
            try {
            	System.out.println(pass);
                // Encrypt password before saving
                String salt = encryptionService.generateSalt();
                String encryptedPassword = encryptionService.encrypt(pass.getPassword(), pass.getEmail(), salt);

                // Set encrypted password and salt in the PasswordManager object
                pass.setPassword(encryptedPassword);
                pass.setSalt(salt);

                PasswordManager p = passRepo.save(pass);
                
                if (p != null) {
                    isAdd = true;
                }
            } catch (Exception e) {
                e.printStackTrace(); // Handle the exception properly
            }
        }
        return isAdd;
    }

    // Get all passwords by email with pagination
    public Page<PasswordManager> allPassManagers(String email, int current_page) {
        Pageable pageable = PageRequest.of(current_page, 5);

        Page<PasswordManager> passPage = passRepo.findByEmail(email, pageable);

        // Decrypt the password for each PasswordManager before returning
        passPage = passPage.map(passManager -> {
            try {
                String decryptedPassword = encryptionService.decrypt(passManager.getPassword(), passManager.getEmail(), passManager.getSalt());
                passManager.setPassword(decryptedPassword);
            } catch (Exception e) {
                e.printStackTrace(); // Handle decryption error appropriately
            }
            return passManager;
        });

        return passPage;
    }
    // Find a record by ID
    public PasswordManager findRecordById(int id) {
        return passRepo.findById(id);
    }

    // Delete a record by ID
    public void deleteById(int id) {
        passRepo.deleteById(id);
    }

    // Update a password record
    public PasswordManager update(PasswordManager newPasswordManager, int id) {
        PasswordManager existingPassManager = passRepo.findById(id);
        if (existingPassManager != null) {
            try {
                // Encrypt the new password before updating
                String salt = encryptionService.generateSalt();
                String encryptedPassword = encryptionService.encrypt(newPasswordManager.getPassword(), newPasswordManager.getEmail(), salt);

                newPasswordManager.setId(id);
                newPasswordManager.setPassword(encryptedPassword);
                newPasswordManager.setSalt(salt);

                return passRepo.save(newPasswordManager);
            } catch (Exception e) {
                e.printStackTrace(); // Handle the exception
            }
        }
        return null;
    }
}
