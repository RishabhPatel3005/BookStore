package com.bookstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.entities.User;
import com.bookstore.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/add")
	public ResponseEntity<User> addUser(@RequestBody User user){
		User saveduser = userService.saveUser(user);
		return ResponseEntity.ok(saveduser);
	}
	
	@PostMapping("/purcase")
	public ResponseEntity<String> buyBook(@RequestParam Long userId,@RequestParam Long bookId,@RequestParam int quantity){
		String response = userService.purchaseBook(userId, bookId, quantity);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/login") 
    public ResponseEntity<String> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok("Login successful");
    }
}
