package imoveis.aluguel.repositories;

import imoveis.aluguel.entities.PropertyLog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyLogRepository extends JpaRepository<PropertyLog, Long> {
     List<PropertyLog> findAllByPropertyIdOrderByIdDesc(Long propertyId);
}