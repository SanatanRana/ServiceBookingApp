package com.example.provider_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProvider {
    @Id
    private Long id;
    private String name;
    private String serviceType;
    private String experience;
    private String city;
    private boolean available;
}
