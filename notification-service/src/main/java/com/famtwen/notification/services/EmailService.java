package com.famtwen.notification.services;

import com.famtwen.notification.dto.request.EmailRequest;
import com.famtwen.notification.dto.request.SendEmailRequest;
import com.famtwen.notification.dto.request.Sender;
import com.famtwen.notification.dto.response.EmailResponse;
import com.famtwen.notification.exception.AppException;
import com.famtwen.notification.exception.ErrorCode;
import com.famtwen.notification.repository.httpclient.EmailClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    EmailClient emailClient;

    @Value("${brevo.api-key}")
    @NonFinal
    private String apiKey;

    @Value("${brevo.sender.name}")
    @NonFinal
    private String name;

    @Value("${brevo.sender.email}")
    @NonFinal
    private String email;

    public EmailResponse sendEmail(SendEmailRequest request) {
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .name(name)
                        .email(email)
                        .build())
                .to(List.of(request.getTo()))
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();
        try {
            return emailClient.sendEmail(apiKey, emailRequest);
        } catch (FeignException e){
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}
