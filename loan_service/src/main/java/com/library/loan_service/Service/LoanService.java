package com.library.loan_service.Service;

import com.library.common.event.LoanEvent;
import com.library.loan_service.client.BookClient;
import com.library.loan_service.client.UserClient;
import com.library.loan_service.Entity.Loan;
import com.library.loan_service.Entity.*;
import com.library.loan_service.Repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoanService {

    private final BookClient bookClient;
    private final UserClient userClient;
    private final LoanRepository loanRepository;
    private final RabbitTemplate rabbitTemplate;

    public Loan createLoan(String bookId, String bearerToken) {
        log.info("Creating loan for bookId: {}, with token: {}", bookId, bearerToken);

        // 1. Validate user
        log.info("Calling user-service for profile");
        ResponseEntity<UserClient.UserDto> userResponse = userClient.getCurrentUser(bearerToken);
        log.info("User response status: {}", userResponse.getStatusCode());
        if (!userResponse.getStatusCode().is2xxSuccessful() || userResponse.getBody() == null) {
            log.error("User validation failed with status: {}", userResponse.getStatusCode());
            throw new RuntimeException("Invalid user or token");
        }
        UserClient.UserDto user = userResponse.getBody();
        log.info("User validated: {}", user.email());

        // 2. Check availability
        log.info("Calling book-service for available copies");
        Integer copies = bookClient.getAvailableCopies(bookId);
        log.info("Available copies: {}", copies);
        if (copies == null || copies <= 0) {
            log.error("No copies available for bookId: {}", bookId);
            throw new RuntimeException("No copies available");
        }

        // 3. Decrement
        log.info("Calling book-service to decrement copies");
        BookClient.BookDto decrementedBook = bookClient.decrementCopies(bookId);
        if (decrementedBook == null) {
            log.error("Failed to decrement copies for bookId: {}", bookId);
            throw new RuntimeException("Failed to decrement book copies");
        }
        log.info("Copies decremented to: {}", decrementedBook.availableCopies());

        // 4. Save loan
        Loan loan = Loan.builder()
                .bookId(bookId)
                .userId(user.id())
                .userEmail(user.email())
                .loanDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(14))
                .status(LoanStatus.ACTIVE)
                .build();
        Loan saved = loanRepository.save(loan);
        log.info("Loan saved with id: {}", saved.getId());

        // 5. Publish event
        log.info("Publishing loan.created event");
        // Inside LoanService.createLoan(), replace the send line with:
        rabbitTemplate.convertAndSend("loan.exchange", "loan.created",
                new LoanEvent(saved.getId(), user.email(), bookId));
        return saved;
    }
}