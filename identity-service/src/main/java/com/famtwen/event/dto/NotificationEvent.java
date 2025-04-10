package com.famtwen.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationEvent {
    String chanel;
    String recipient;
    String templateCode; // Noi dung se co nhieu loai: welcome, notify, otp,..
    Map<String, Object> param; // Param chen vao nhung cho duc lo template code, pram co the la info user
    String subject;
    String body;
}
