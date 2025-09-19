package imoveis.aluguel.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import imoveis.aluguel.entities.Property;
import imoveis.aluguel.mappers.PropertyMapper;
import imoveis.aluguel.repositories.PersonRepository;
import imoveis.aluguel.repositories.PropertyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final PersonRepository personRepository;
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
        
        propertyMapper.updateEntity(updatedProperty, recordedProperty);

        var updatedPersonId = updatedProperty.getPerson().getId();
        var recordedPersonId = recordedProperty.getPerson().getId();

        if(!recordedPersonId.equals(updatedPersonId)) {
            var newPerson = personRepository.findById(updatedPersonId).orElse(null);
            recordedProperty.setPerson(newPerson);
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
