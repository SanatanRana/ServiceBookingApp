package com.example.payment_service.service;

import com.example.payment_service.entity.Payment;
import com.example.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment processPayment(Payment paymentRequest) {
        // Mock payment processing logic
        boolean isSuccess = new Random().nextBoolean();
        paymentRequest.setStatus(isSuccess ? "SUCCESS" : "FAILED");
        return paymentRepository.save(paymentRequest);
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
    }
}
