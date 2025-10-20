package imoveis.aluguel.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import imoveis.aluguel.entities.PropertyLog;
import imoveis.aluguel.repositories.PropertyLogRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PropertyLogServiceImpl implements PropertyLogService {

    private final PropertyLogRepository propertyLogRepository;

    @Override
    public List<PropertyLog> findAllByPropertyId(Long id) {

        return propertyLogRepository.findAllByPropertyIdOrderByIdDesc(id);

    }

    @Override
    public Optional<PropertyLog> findLastRelevantChangeByPropertyId(Long propertyId) {

        return propertyLogRepository.findLastRelevantChangeByPropertyId(propertyId);

    }

}
