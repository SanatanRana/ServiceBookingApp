package com.example.provider_service.service;

import com.example.provider_service.entity.ServiceEntity;
import com.example.provider_service.entity.ServiceProvider;
import com.example.provider_service.repository.ServiceEntityRepository;
import com.example.provider_service.repository.ServiceProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final ServiceProviderRepository providerRepository;
    private final ServiceEntityRepository serviceRepository;

    public ServiceProvider createProvider(ServiceProvider provider) {
        return providerRepository.save(provider);
    }

    public ServiceProvider getProviderById(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Provider not found with id: " + id));
    }

    public ServiceEntity addServiceToProvider(Long providerId, ServiceEntity service) {
        service.setProviderId(providerId);
        return serviceRepository.save(service);
    }

    public List<ServiceEntity> getServicesByProviderId(Long providerId) {
        return serviceRepository.findByProviderId(providerId);
    }
}
