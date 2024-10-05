package com.bookstore.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.entities.Revenue;

public interface RevenueRepository extends JpaRepository<Revenue, Long>{

	List<Revenue> findByAuthorId(Long authorId);
}
