package imoveis.aluguel.services;

import java.util.List;

import org.springframework.data.domain.Sort;

import imoveis.aluguel.entities.Property;

public interface PropertyService {

    Property create(Property property);

    Property findById(Long id);

    Property update(Long id, Property updatedProperty);

    List<Property> list(Sort sort);

    void deleteById(Long id);
    
}
