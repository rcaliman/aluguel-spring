package imoveis.aluguel.services;

import java.util.List;
import java.util.Optional;

import imoveis.aluguel.entities.PropertyLog;

public interface PropertyLogService {

    List<PropertyLog> findAllByPropertyId(Long id);

    Optional<PropertyLog> findLastRelevantChangeByPropertyId(Long propertyId);

}
