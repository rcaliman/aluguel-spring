package imoveis.aluguel.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import imoveis.aluguel.entities.PropertyLog;

public interface PropertyLogRepository extends JpaRepository<PropertyLog, Long> {

    List<PropertyLog> findAllByPropertyIdOrderByIdDesc(Long id);
    
}
