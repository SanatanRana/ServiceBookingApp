package com.example.provider_service.controller;

import com.example.provider_service.entity.ServiceEntity;
import com.example.provider_service.entity.ServiceProvider;
import com.example.provider_service.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @PostMapping
    public ServiceProvider createProviderProfile(@RequestBody ServiceProvider provider) {
        return providerService.createProvider(provider);
    }

    @GetMapping("/{id}")
    public ServiceProvider getProviderProfile(@PathVariable Long id) {
        return providerService.getProviderById(id);
    }

    @PostMapping("/{providerId}/services")
    public ServiceEntity addService(@PathVariable Long providerId, @RequestBody ServiceEntity service) {
        return providerService.addServiceToProvider(providerId, service);
    }

    @GetMapping("/{providerId}/services")
    public List<ServiceEntity> getServices(@PathVariable Long providerId) {
        return providerService.getServicesByProviderId(providerId);
    }
}
