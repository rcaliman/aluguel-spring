package imoveis.aluguel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import imoveis.aluguel.entities.Property;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    
}
