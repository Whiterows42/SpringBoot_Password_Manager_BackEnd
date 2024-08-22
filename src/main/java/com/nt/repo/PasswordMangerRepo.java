package com.nt.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nt.entity.PasswordManager;

@Repository
public interface PasswordMangerRepo extends CrudRepository<PasswordManager, Integer> {

	List<PasswordManager> findAll();
	PasswordManager findById(int id);
	List<PasswordManager> findByEmail(String email);
}
