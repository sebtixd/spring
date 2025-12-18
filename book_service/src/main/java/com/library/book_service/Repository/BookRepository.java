package com.library.book_service.Repository;


import com.library.book_service.Entity.Book;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Mono<Book> findByIsbn(String isbn);

    // Atomic decrement for loan safety
    Mono<Book> findAndModifyByIdAndAvailableCopiesGreaterThan(
            String id, int availableCopies, Book updatedBook);
    @Query("{'_id': ?0, 'availableCopies': {$gt: 0}}")
    Mono<Book> findAndDecrementCopies(String id);

    @Tailable
    @Query("{}")
    Flux<Book> streamAll(); // if you ever want change events

}
