package com.library.notification_service.listener;


import com.library.common.event.LoanEvent;
import com.library.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoanListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = "loan.queue")
    public void handleLoanCreated(LoanEvent event) {  // ← Use the shared DTO
        notificationService.sendLoanNotification(event);
    }
}
