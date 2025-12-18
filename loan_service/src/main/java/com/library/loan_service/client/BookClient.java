package com.library.loan_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "book-service", url = "http://localhost:8083")  // ← ADD url HERE
 public interface BookClient {

    @GetMapping("/api/books/{id}/available-copies")
    Integer getAvailableCopies(@PathVariable("id") String id);

    @PatchMapping("/api/books/{id}/decrement-copies")
    BookDto decrementCopies(@PathVariable("id") String id);

    public record BookDto(String id, Integer availableCopies) {}
}