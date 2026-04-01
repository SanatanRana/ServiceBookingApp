package com.example.payment_service.controller;

import com.example.payment_service.entity.Payment;
import com.example.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public Payment processPayment(@RequestBody Payment paymentRequest) {
        return paymentService.processPayment(paymentRequest);
    }

    @GetMapping("/{id}")
    public Payment getPaymentStatus(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }
}
