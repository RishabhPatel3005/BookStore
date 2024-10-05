package com.bookstore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bookstore.entities.Revenue;
import com.bookstore.repositories.RevenueRepository;

@RestController
@RequestMapping("/revenue")
public class RevenueController {

	@Autowired
	private RevenueRepository revenueRepository;
	
	@GetMapping("/author/{authorId}")
	@PreAuthorize("hasRole('AUTHOR')")
	public List<Revenue> getAuthorRevenue(@PathVariable("authorId") Long authorId){
		return revenueRepository.findByAuthorId(authorId);
	}
}
