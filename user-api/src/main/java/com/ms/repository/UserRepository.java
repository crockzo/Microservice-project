package com.ms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.ms.model.User;

public interface UserRepository extends JpaRepository<User, String>{

	Optional<User> findByUsername(String username);
	
	@Modifying
	void deleteByUsername(String username);
	
	boolean existsByUsername(String username);
}
