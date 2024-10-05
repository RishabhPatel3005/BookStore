package com.bookstore.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bookstore.entities.Book;
import com.bookstore.entities.PurchaseHistory;
import com.bookstore.entities.Revenue;
import com.bookstore.entities.User;
import com.bookstore.repositories.BookRepository;
import com.bookstore.repositories.PurchaseHistoryRepository;
import com.bookstore.repositories.RevenueRepository;
import com.bookstore.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private PurchaseHistoryRepository purchaseHistoryRepository;
	
	@Autowired
	private RevenueRepository revenueRepository;
	
	public User saveUser(User user) {
		User save = userRepository.save(user);
		return save;
	}
	
	public String purchaseBook(Long userId, Long bookId, int quantity) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0.");
        }

        int totalPrice = book.getPrice() * quantity;

        PurchaseHistory purchaseHistory = new PurchaseHistory();
        purchaseHistory.setUser(user);
        purchaseHistory.setBook(book);
        purchaseHistory.setQuantity(quantity);
        purchaseHistory.setPrice(totalPrice);
        purchaseHistory.setPurchaseDate(LocalDate.now());

        purchaseHistoryRepository.save(purchaseHistory);

        book.setSellCount(book.getSellCount() + quantity);
        bookRepository.save(book);
        
        
        
        User loggedInAuthor = getLoggedInUser(); 
        Revenue revenue = null;
        if (book.getAuthors().contains(loggedInAuthor)) {
            revenue = new Revenue();
            revenue.setAuthor(loggedInAuthor); 
            revenue.setBookName(book.getTitle());
            revenue.setBookPrice(book.getPrice());
            revenue.setBookQuantity(quantity);
            revenue.setSale(book.getPrice() * quantity);
        }
            revenueRepository.save(revenue);
        return "Book purchased successfully revenue updated for the author";
	}
	
	public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get the username of the logged-in user
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
    }
	
	

}
