package imoveis.aluguel.services;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import imoveis.aluguel.dtos.PropertyDtoResponse;
import imoveis.aluguel.entities.Property;
import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.mappers.PropertyLogMapper;
import imoveis.aluguel.mappers.PropertyMapper;
import imoveis.aluguel.repositories.PropertyRepository;
import imoveis.aluguel.repositories.TenantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final TenantRepository tenantRepository;
    private final PropertyMapper propertyMapper;
    private final PropertyLogMapper propertyLogMapper;

    @Override
    @CacheEvict(value = "properties", allEntries = true)
    public PropertyDtoResponse create(Property property) {

        var propertyLog = propertyLogMapper.toPropertyLog(property);
        property.addPropertyLog(propertyLog);
        var savedProperty = propertyRepository.save(property);

        return propertyMapper.toDtoResponse(savedProperty);

    }

    @Override
    @Cacheable("properties")
    public PropertyDtoResponse findById(Long id) {

        var property = propertyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Imovel de id %d não encontrado", id)));

        return propertyMapper.toDtoResponse(property);
        
    }

    @Override
    @Transactional
    @CacheEvict(value = "properties", allEntries = true)
    public PropertyDtoResponse update(Long id, Property updatedProperty) {

        Property originalProperty = propertyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Imovel de id %d não encontrado", id)));

        propertyMapper.updateEntity(updatedProperty, originalProperty);

        Tenant updatedTenant = updatedProperty.getTenant();

        if (updatedTenant == null || updatedTenant.getId() == null) {

            originalProperty.setTenant(null);

        } else {

            Tenant tenant = tenantRepository.findById(updatedTenant.getId()).orElse(null);
            originalProperty.setTenant(tenant);
            
        }

        var propertyLog = propertyLogMapper.toPropertyLog(originalProperty);
        originalProperty.addPropertyLog(propertyLog);

        var savedProperty =  propertyRepository.save(originalProperty);

        return propertyMapper.toDtoResponse(savedProperty);

    }

    @Override
    @Cacheable(value = "properties")
    public List<PropertyDtoResponse> list(String sortField) {
        return switch (sortField) {
            case "tenant.name" -> 
                    propertyRepository.findAllOrderByTenantNameAsc().stream()
                        .map(propertyMapper::toDtoResponse)
                        .toList();

            case "propertyType" -> 
                    propertyRepository.findAllOrderByPropertyTypeAsc().stream()
                        .map(propertyMapper::toDtoResponse)
                        .toList();

            case "number" -> 
                    propertyRepository.findAllOrderByNumberAsc().stream()
                        .map(propertyMapper::toDtoResponse)
                        .toList();

            case "paymentDay" -> 
                    propertyRepository.findAllOrderByPaymentDayAsc().stream()
                        .map(propertyMapper::toDtoResponse)
                        .toList();

            case "value" -> 
                    propertyRepository.findAllOrderByValueAsc().stream()
                        .map(propertyMapper::toDtoResponse)
                        .toList();

            default -> 
                    propertyRepository.findAllOrderByTenantNameAsc().stream()
                        .map(propertyMapper::toDtoResponse)
                        .toList();

        };
    }

    @Override
    @Transactional
    @CacheEvict(value = "properties", allEntries = true)
    public void deleteById(Long id) {

        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Imóvel de id %d não encontrado.", id)));
        propertyRepository.delete(property);

    }
}