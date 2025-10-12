package imoveis.aluguel.services;

import java.util.List;

import imoveis.aluguel.entities.Property;

public interface PropertyService {

    Property create(Property property);

    Property findById(Long id);

    Property update(Long id, Property updatedProperty);

    List<Property> list(String sortField);

    void deleteById(Long id);


}
