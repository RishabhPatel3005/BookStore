package com.bookstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.entities.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

}
