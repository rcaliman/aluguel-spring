package imoveis.aluguel.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import imoveis.aluguel.entities.Property;
import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.mappers.PropertyMapper;
import imoveis.aluguel.repositories.PropertyRepository;
import imoveis.aluguel.repositories.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final TenantRepository tenantRepository;
    private final PropertyMapper propertyMapper;

    @Override
    public Property create(Property property) {
        return propertyRepository.save(property);
    }

    @Override
    public Property findById(Long id) {
        return propertyRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException(String.format("Imovel de id %d dão encontrado", id))
        );
    }

    @Override
    @Transactional
    public Property update(Long id, Property updatedProperty) {
        Property recordedProperty = propertyRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException(String.format("Imovel de id %d dão encontrado", id))
        );
        
        // Atualiza os campos simples primeiro (ex: endereço, valor, etc.)
        propertyMapper.updateEntity(updatedProperty, recordedProperty);

        // Lógica segura para atualizar o inquilino, tratando casos de imóvel vago
        Tenant tenantInRequest = updatedProperty.getTenant();
        if (tenantInRequest == null || tenantInRequest.getId() == null) {
            // Se o formulário enviou um inquilino nulo ou sem ID, o imóvel fica vago
            recordedProperty.setTenant(null);
        } else {
            // Se enviou um inquilino, busca a entidade completa e a associa
            Tenant newTenant = tenantRepository.findById(tenantInRequest.getId()).orElse(null);
            recordedProperty.setTenant(newTenant);
        }
        
        return propertyRepository.save(recordedProperty);
    }

    @Override
    public List<Property> list(Sort sort) {
        var properties = propertyRepository.findAll(sort);
        return properties;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Property property = propertyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Imóvel de id %d não encontrado.", id)));
        propertyRepository.delete(property);
    }
}