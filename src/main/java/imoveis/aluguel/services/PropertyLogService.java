package imoveis.aluguel.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import imoveis.aluguel.entities.PropertyLog;
import imoveis.aluguel.repositories.PropertyLogRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PropertyLogService {

    private final PropertyLogRepository propertyLogRepository;

    public List<PropertyLog> findAllByPropertyId(Long id) {

        return propertyLogRepository.findAllByPropertyIdOrderByIdDesc(id);

    }

    public Optional<PropertyLog> findLastRelevantChangeByPropertyId(Long propertyId) {

        return propertyLogRepository.findLastRelevantChangeByPropertyId(propertyId);

    }

}
