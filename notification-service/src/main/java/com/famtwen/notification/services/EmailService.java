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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    EmailClient emailClient;

    @Value("${notification.brevo-apikey}")
    @NonFinal
    String apiKey;

    @Value("${notification.name}")
    @NonFinal
    String nameNotify;

    @Value("${notification.email}")
    @NonFinal
    String emailNotify;

    public EmailResponse sendEmail(SendEmailRequest request) {
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .name(nameNotify)
                        .email(emailNotify)
                        .build())
                .to(List.of(request.getTo()))
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();
        if (apiKey.length() <= 64 ) log.error("KEY ERORR: {}", apiKey);
        try {
            return emailClient.sendEmail(apiKey, emailRequest);
        } catch (FeignException e){
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}
