package com.aston.shop.users.repository;

import com.aston.shop.users.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	Optional<User> findByPassword(String password);
	Optional<User> findByUsername(String username);
}
