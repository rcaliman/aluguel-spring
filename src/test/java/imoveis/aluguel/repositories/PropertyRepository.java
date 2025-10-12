package imoveis.aluguel.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import imoveis.aluguel.entities.Property;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query(value = "SELECT p.* FROM tb_properties p LEFT JOIN tb_tenants t ON p.tenant_id = t.id " +
           "ORDER BY " +
           "CASE WHEN :sortField = 'tenant.name' THEN IF(t.name IS NULL, 1, 0) " +
           "     WHEN :sortField = 'propertyType' THEN IF(p.property_type IS NULL, 1, 0) " +
           "     WHEN :sortField = 'number' THEN IF(p.number IS NULL, 1, 0) " +
           "     WHEN :sortField = 'paymentDay' THEN IF(p.payment_day IS NULL, 1, 0) " +
           "     WHEN :sortField = 'value' THEN IF(p.rental_value IS NULL, 1, 0) " +
           "     ELSE IF(t.name IS NULL, 1, 0) END, " +
           "CASE WHEN :sortField = 'tenant.name' THEN t.name " +
           "     WHEN :sortField = 'propertyType' THEN p.property_type " +
           "     WHEN :sortField = 'number' THEN p.number " +
           "     WHEN :sortField = 'paymentDay' THEN p.payment_day " +
           "     WHEN :sortField = 'value' THEN p.rental_value " +
           "     ELSE t.name END ASC", 
           nativeQuery = true)
    List<Property> findAllOrderByAsc(@Param("sortField") String sortField);
}