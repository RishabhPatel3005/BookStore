package com.bookstore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bookstore.entities.PurchaseHistory;
import com.bookstore.repositories.PurchaseHistoryRepository;

@RestController
@RequestMapping("/purchase")
public class PurchaseHistoryController {
	
	@Autowired
	private PurchaseHistoryRepository purchaseHistoryRepository;
	
	@GetMapping("/user/{userId}")
	@PreAuthorize("hasRole('RETAIL_USER')")
    public ResponseEntity<List<PurchaseHistory>> getPurchaseHistory(@PathVariable("userId") Long userId) {
        List<PurchaseHistory> purchaseHistoryList = purchaseHistoryRepository.findByUserId(userId);
        if (purchaseHistoryList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(purchaseHistoryList);
    } 
}
