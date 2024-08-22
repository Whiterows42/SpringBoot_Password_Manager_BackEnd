package com.nt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	public List<PasswordManager> allPassManagers(String email){
	List<PasswordManager> list =	passRepo.findByEmail(email);
	
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
