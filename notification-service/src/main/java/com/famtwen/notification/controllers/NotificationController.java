package com.famtwen.notification.controllers;

import com.famtwen.event.dto.NotificationEvent;
import com.famtwen.notification.dto.request.Recipient;
import com.famtwen.notification.dto.request.SendEmailRequest;
import com.famtwen.notification.services.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    EmailService emailService;

    @KafkaListener(topics = "notification-delivery")
    public void listenNotificationDelivery(NotificationEvent message) {
        log.info("Message received: {}", message);
        emailService.sendEmail(SendEmailRequest.builder()
                                               .to(Recipient.builder()
                                                            .email(message.getRecipient())
                                                            .build())
                                               .subject(message.getSubject())
                                               .htmlContent(message.getBody())
                                               .build());
    }
}
