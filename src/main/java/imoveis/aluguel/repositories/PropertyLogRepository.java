package imoveis.aluguel.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import imoveis.aluguel.entities.PropertyLog;

public interface PropertyLogRepository extends JpaRepository<PropertyLog, Long> {

    List<PropertyLog> findAllByPropertyIdOrderByIdDesc(Long id);

    @Query("""
        SELECT pl FROM PropertyLog pl
        WHERE pl.property.id = :propertyId
        AND (pl.value IS NOT NULL OR pl.tenantName IS NOT NULL OR pl.tenantCpfCnpj IS NOT NULL)
        ORDER BY pl.createdAt DESC
        LIMIT 1
        """)
    Optional<PropertyLog> findLastRelevantChangeByPropertyId(@Param("propertyId") Long propertyId);

}
