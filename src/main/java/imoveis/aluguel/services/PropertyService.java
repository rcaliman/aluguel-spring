package imoveis.aluguel.services;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final TenantRepository tenantRepository;
    private final PropertyMapper propertyMapper;
    private final PropertyLogMapper propertyLogMapper;

    @CacheEvict(value = { "properties", "tenants" }, allEntries = true)
    public Property create(Property property) {

        var propertyLog = propertyLogMapper.toPropertyLog(property);
        property.addPropertyLog(propertyLog);

        return propertyRepository.save(property);

    }

    @Cacheable(value = "properties", key = "#id")
    public Property findById(Long id) {

        return propertyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Imovel de id %d não encontrado", id)));

    }

    @Transactional
    @CacheEvict(value = { "properties", "tenants" }, allEntries = true)
    public Property update(Long id, Property updatedProperty) {

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

        return propertyRepository.save(originalProperty);

    }

    @Cacheable(value = "properties", key = "'list-' + #sortField")
    public List<Property> list(String sortField) {
        return switch (sortField) {
            case "tenant.name" ->
                propertyRepository.findAllOrderByTenantNameAsc();

            case "propertyType" ->
                propertyRepository.findAllOrderByPropertyTypeAsc();

            case "number" ->
                propertyRepository.findAllOrderByNumberAsc();

            case "paymentDay" ->
                propertyRepository.findAllOrderByPaymentDayAsc();

            case "value" ->
                propertyRepository.findAllOrderByValueAsc();

            default ->
                propertyRepository.findAllOrderByTenantNameAsc();

        };
    }

    @Transactional
    @CacheEvict(value = { "properties", "tenants" }, allEntries = true)
    public void deleteById(Long id) {

        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Imóvel de id %d não encontrado.", id)));

        propertyRepository.delete(property);

    }
}