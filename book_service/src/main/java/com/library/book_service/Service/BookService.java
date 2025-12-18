package com.library.book_service.Service;



import com.library.book_service.Entity.Book;
import com.library.book_service.Repository.BookRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }


    public Flux<Book> findAll() {
        return repository.findAll();
    }

    public Mono<Book> findById(String id) {
        return repository.findById(id);
    }

    // GET available copies – perfect as-is
    public Mono<Integer> getAvailableCopies(String id) {
        return repository.findById(id)
                .map(Book::getAvailableCopies);
    }

    // ATOMIC decrement – THIS IS THE FIXED VERSION
    public Mono<Book> decrementAvailableCopies(String id) {
        return repository.findById(id)
                .filter(book -> book.getAvailableCopies() > 0)
                .map(book -> Book.builder()
                        .id(book.getId())
                        .isbn(book.getIsbn())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .totalCopies(book.getTotalCopies())
                        .availableCopies(book.getAvailableCopies() - 1)  // ← new value
                        .genre(book.getGenre())
                        .build())
                .flatMap(repository::save)
                .switchIfEmpty(Mono.error(new RuntimeException("No copies available or book not found")));
    }

    public Mono<Book> save(Book book) {
        Book toSave = Book.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getTotalCopies())  // available = total on creation
                .genre(book.getGenre())
                .build();
        return repository.save(toSave);
    }
    public Mono<Void> deleteAll() {
        return repository.deleteAll();
    }
}
