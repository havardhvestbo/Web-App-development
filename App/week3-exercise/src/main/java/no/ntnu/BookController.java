package no.ntnu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {
    
    List<Book> books = new ArrayList<>();

    BookController() {
        initializeData();
    }
    
    private void initializeData() {
        books.add(new Book(1,"John", 2008, 504));
        books.add(new Book(2,"Karl", 2009, 580));
        books.add(new Book(3,"B", 2005, 507));
        books.add(new Book(4,"Donny", 2002, 504));
    }

    @GetMapping("/books")
    private Iterable<Book>  getBooks() {
        return books;
    }

    @GetMapping("/books/{id}")
    ResponseEntity<Object> getBook(@PathVariable int id) {
        ResponseEntity<Object> response;
        Book book = findBookById(id);
        if (book != null) {
            response = new ResponseEntity<>(book, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>("No book found with id = " + id,HttpStatus.NOT_FOUND);
        }
        return response;
    }

    private Book findBookById(int id) {
        Book foundBook = null;
        Iterator<Book>  it = books.iterator();

        while(foundBook == null && it.hasNext()) {
            Book b = it.next();
            if (b.id() == id) {
                foundBook = b;
            }
        }
        return foundBook;
    }

    @PostMapping("/books")
    ResponseEntity<String> addBook(@RequestBody Book book) {
        ResponseEntity<String> response;
    
        try {
          addBookToCollection(book);
          response = new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
          response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    
        return response;
      }

    private void addBookToCollection(Book book) throws IllegalArgumentException{
        checkIfBookIsValid(book);
        int newId = books.size() + 1;
        Book copy = new Book(newId, book.title(), book.year(), book.numberOfPages());
        books.add(copy);
    }

    private void checkIfBookIsValid(Book book) throws IllegalArgumentException {
        if (book.title() != null) {
            throw new IllegalArgumentException("Book title can not be null");
        }
    }
}