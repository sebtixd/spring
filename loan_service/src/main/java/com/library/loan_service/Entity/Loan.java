package com.library.loan_service.Entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookId;
    private Long userId;
    private String userEmail;

    private LocalDateTime loanDate = LocalDateTime.now();
    private LocalDateTime dueDate = LocalDateTime.now().plusDays(14);
    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    private LoanStatus status = LoanStatus.ACTIVE;
}

