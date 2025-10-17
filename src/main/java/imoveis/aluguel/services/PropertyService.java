package imoveis.aluguel.services;

import java.util.List;

import imoveis.aluguel.dtos.PropertyDtoResponse;
import imoveis.aluguel.entities.Property;

public interface PropertyService {

    PropertyDtoResponse create(Property property);

    PropertyDtoResponse findById(Long id);

    PropertyDtoResponse update(Long id, Property updatedProperty);

    List<PropertyDtoResponse> list(String sortField);

    void deleteById(Long id);


}
