package com.example.booking_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long bookingId;
    private double amount;
    private String status;
    private String paymentMethod;
}
