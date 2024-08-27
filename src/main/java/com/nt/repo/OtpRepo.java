package com.nt.repo;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.entity.OtpEntity;
import java.util.List;


@Repository
public interface OtpRepo extends JpaRepository<OtpEntity, Long> {

	 void deleteByCreatedAtBefore(LocalDateTime expiryTime);
	
	 
	 List<OtpEntity> findByEmail(String email);
	
}
