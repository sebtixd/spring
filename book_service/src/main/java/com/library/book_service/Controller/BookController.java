package com.library.book_service.Controller;

import com.library.book_service.Entity.Book;
import com.library.book_service.Service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @GetMapping
    public Flux<Book> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Book> getById(@PathVariable String id) {
        return service.findById(id);
    }

    @GetMapping("/{id}/available-copies")
    public Mono<Integer> getAvailableCopies(@PathVariable String id) {
        return service.getAvailableCopies(id);
    }

    // THIS IS THE ONLY CHANGE — return a simple DTO, not full Book
    @PatchMapping("/{id}/decrement-copies")
    public Mono<BookDto> decrementCopies(@PathVariable String id) {
        return service.decrementAvailableCopies(id)
                .map(book -> new BookDto(book.getId(), book.getAvailableCopies()));
    }
    @DeleteMapping("/all")
    public Mono<Void> deleteAll() {
        return service.deleteAll();
    }


    @PostMapping
    public Mono<ResponseEntity<?>> create(@RequestBody Book book) {
        return service.save(book)
                .map(saved -> ResponseEntity.ok(saved));
    }

    // ADD THIS DTO INSIDE THE SAME FILE (or in client package)
    public record BookDto(String id, Integer availableCopies) {}
}