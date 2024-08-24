package com.nt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.nt.entity.PasswordManager;
import com.nt.repo.PasswordMangerRepo;

@Service
public class PasswordMangerService {

	@Autowired
	private PasswordMangerRepo passRepo;
	
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
	public boolean storeNewPassword(PasswordManager pass){
		
		boolean isAdd = false;
		
		if (pass != null && pass.getPassword() != null) {
            // Encrypt the password before saving
//            String encryptedPassword = passwordEncoder.encode(pass.getPassword());
//            pass.setPassword(encryptedPassword);
				
            PasswordManager p = passRepo.save(pass);
            if (p != null) {
                isAdd = true;
            }
        }
		
		
		return isAdd;
		
	}
	
	
	
	public Page<PasswordManager> allPassManagers(String email , int current_page){
	Pageable pageable =	PageRequest.of(current_page, 5);
	
	Page<PasswordManager> list =	passRepo.findByEmail(email , pageable);
	
	return list;
	}
	
	
	
	
	public PasswordManager findrecoredByid(int id) {
		
		
	return	passRepo.findById(id);
	
	}
	
	
	public void isDeletebyId(int id) {
		
		passRepo.deleteById(id);
	
	}
	
	
	public PasswordManager update(PasswordManager p, int id) {
		
		PasswordManager updatedManager = null;
	PasswordManager pass =	passRepo.findById(id);
		if (pass != null) {
			
			p.setId(id);
		updatedManager =	passRepo.save(p);
		}
		
		return updatedManager;
	}
}
