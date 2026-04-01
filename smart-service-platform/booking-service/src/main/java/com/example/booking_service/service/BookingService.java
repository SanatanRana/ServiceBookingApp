package com.example.booking_service.service;

import com.example.booking_service.dto.PaymentDto;
import com.example.booking_service.entity.Booking;
import com.example.booking_service.feign.PaymentClient;
import com.example.booking_service.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PaymentClient paymentClient;

    public Booking createBooking(Booking booking) {
        booking.setStatus("PENDING");
        Booking savedBooking = bookingRepository.save(booking);

        // Process payment via Feign (flat fee 500 for demo)
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setBookingId(savedBooking.getId());
        paymentDto.setAmount(500);
        paymentDto.setPaymentMethod("CARD");

        PaymentDto response = paymentClient.processPayment(paymentDto);
        if ("SUCCESS".equals(response.getStatus())) {
            savedBooking.setStatus("CONFIRMED");
        } else {
            savedBooking.setStatus("FAILED");
        }
        return bookingRepository.save(savedBooking);
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getBookingsByProviderId(Long providerId) {
        return bookingRepository.findByProviderId(providerId);
    }
}
