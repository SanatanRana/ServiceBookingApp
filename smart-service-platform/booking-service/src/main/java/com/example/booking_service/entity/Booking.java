package com.example.booking_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long providerId;
    private Long serviceId;
    private String bookingDate;
    private String status; // PENDING, CONFIRMED, COMPLETED, CANCELLED, FAILED
}
