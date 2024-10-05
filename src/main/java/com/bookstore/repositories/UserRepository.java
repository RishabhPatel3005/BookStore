package com.bookstore.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.entities.User;
import com.bookstore.entities.User.Role;

public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByUsername(String username);

	List<User> findByRole(Role retailUser);

}
