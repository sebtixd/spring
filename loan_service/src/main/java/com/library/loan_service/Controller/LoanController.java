package com.library.loan_service.Controller;

import com.library.loan_service.Entity.Loan;
import com.library.loan_service.Repository.LoanRepository;
import com.library.loan_service.Service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;  // ← ADD THIS
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final LoanRepository loanRepository;

    @PostMapping("/{bookId}")
    public ResponseEntity<?> borrowBook(
            @PathVariable String bookId,
            @RequestHeader("Authorization") String bearerToken) {
        try {
            Loan loan = loanService.createLoan(bookId, bearerToken);
            return ResponseEntity.ok(loan);
        } catch (Exception e) {
            log.error("Borrow book failed for bookId: {}, error: {}", bookId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());  // Or custom error
        }
    }
    @GetMapping
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
}