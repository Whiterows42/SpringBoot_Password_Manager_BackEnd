package com.nt.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nt.entity.User;
import java.util.List;

@Repository
public interface UserRepo extends CrudRepository<User, Integer> {

	List<User> findByEmail(String email);

	List<User> findByEmailAndPassword(String email, String password);

	User findById(int id);
}
