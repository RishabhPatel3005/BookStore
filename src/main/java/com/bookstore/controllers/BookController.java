package com.bookstore.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.entities.Book;
import com.bookstore.entities.User;
import com.bookstore.entities.User.Role;
import com.bookstore.repositories.UserRepository;
import com.bookstore.services.BookService;
import com.bookstore.services.EmailService;

@RestController
@RequestMapping("/books")
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmailService emailService;

	@PostMapping("/add")
	@PreAuthorize("hasRole('AUTHOR') or hasRole('ADMIN')")
	public ResponseEntity<Book> addBook(@RequestBody Book book){
		if(book.getPrice() < 100 || book.getPrice() > 1000) {
			return ResponseEntity.badRequest().build();
		}
		Book newbook = bookService.saveBook(book);
		notifyRetailUsers(newbook);
		return ResponseEntity.ok(newbook);
	}
	
	public void notifyRetailUsers(Book book) {
	    List<User> retailUsers = userRepository.findByRole(Role.RETAIL_USER);
	    
	    String subject = "New Book Release: " + book.getTitle();
	    String body = "Dear user,\n\nWe are excited to inform you that a new book titled \"" 
	                  + book.getTitle() + "\" has been released. Check it out now!\n\nRegards, Bookstore";
	    
	    for (User user : retailUsers) {
	        emailService.sendEmail(user.getUsername(), subject, body);
	    }
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Book> findBook(@PathVariable("id") Long id){
		Book book = (Book) bookService.getBook(id);
		if(book == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.of(Optional.of(book));
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Book>> getBooks(){
		List<Book> list = bookService.getAllBooks();
		if(list.size() <= 0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(list);
	}
	
	@PutMapping("/update/{id}")
	@PreAuthorize("hasRole('AUTHOR') or hasRole('ADMIN')")
	public ResponseEntity<Book> updateBook(@PathVariable("id") Long id,@RequestBody Book book){
		Book updateBook = bookService.updateBook(id, book);
		return ResponseEntity.ok(updateBook);
	}
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('AUTHOR') or hasRole('ADMIN')")
	public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id){
		bookService.deleteBook(id);
		return ResponseEntity.ok().build();
	}
	
	
}
