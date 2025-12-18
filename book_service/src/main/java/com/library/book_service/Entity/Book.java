package com.library.book_service.Entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    private String id;
    private String isbn;
    private String title;
    private String author;
    private Integer totalCopies=0;
    private Integer availableCopies=0;
    private String genre;
}
