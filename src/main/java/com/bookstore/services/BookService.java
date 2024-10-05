package com.bookstore.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.entities.Book;
import com.bookstore.repositories.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;
	
	public Book saveBook(Book book) {
		Book savebook = bookRepository.save(book);
		return savebook;
	}
	
	public Book getBook(Long id) {
		Book book = null;
		try {
			Optional<Book> optionalBook = bookRepository.findById(id);
			if (optionalBook.isPresent()) {
				book = optionalBook.get();
			} else {
				System.out.println("Book not found with id: " + id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return book;

	}
	
	public List<Book> getAllBooks(){
		List<Book> list = bookRepository.findAll();
		return list;
	}
	
	public Book updateBook(Long id,Book updatedBook) {
		Optional<Book> optionalBook = bookRepository.findById(id);
		Book existingBook = null;
		if(optionalBook.isPresent()) {
			existingBook = optionalBook.get();
			existingBook.setTitle(updatedBook.getTitle());
			existingBook.setDescription(updatedBook.getDescription());
			int price = updatedBook.getPrice();
			if(price < 100 || price > 1000) {
				throw new IllegalArgumentException("Price range is 100 to 1000");
			}
			existingBook.setPrice(price);
			existingBook.setSellCount(updatedBook.getSellCount());
			existingBook.setAuthors(updatedBook.getAuthors());
		}
		return bookRepository.save(existingBook);
	}
	
	public void deleteBook(Long id) {
		bookRepository.deleteById(id);
	}
	
	
}
