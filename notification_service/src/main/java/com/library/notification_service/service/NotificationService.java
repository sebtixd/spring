package com.library.notification_service.service;


import com.library.common.event.LoanEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    public void sendLoanNotification(LoanEvent event) {
        log.info("Sending notification for loanId: {}", event.getLoanId());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("neilafakher8@gmail.com");  // Replace with your email
        message.setTo(event.getUserEmail());
        message.setSubject("Book Borrowed Successfully!");
        message.setText("Dear User,\n\nYou have successfully borrowed book ID: " + event.getBookId() + ".\nLoan ID: " + event.getLoanId() + "\n\nReturn by: 14 days.\n\nLibrary Team");

        try {
            mailSender.send(message);
            log.info("Email sent to: {}", event.getUserEmail());
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
        }
    }
}
