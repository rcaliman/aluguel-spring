package imoveis.aluguel.services;

import java.util.List;

import imoveis.aluguel.entities.PropertyLog;

public interface PropertyLogService {

    List<PropertyLog> findAllByPropertyId(Long id);

}
