package com.smartorder.model;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private String email;
    private String subject;
    private String message;

    @Override
    public String toString() {
        return "NotificationEvent{" +
                "email='" + email + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
