package imoveis.aluguel.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import imoveis.aluguel.entities.Property;

public interface PropertyRepository extends JpaRepository<Property, Long> {

       @Query(value = "SELECT p.* FROM tb_properties p LEFT JOIN tb_tenants t ON p.tenant_id = t.id " +
                     "ORDER BY IF(t.name IS NULL, 1, 0), t.name ASC", nativeQuery = true)
       List<Property> findAllOrderByTenantNameAsc();

       @Query(value = "SELECT p.* FROM tb_properties p " +
                     "ORDER BY " +
                     "CASE p.property_type " +
                     "    WHEN 'APARTAMENTO' THEN 'apartamento' " +
                     "    WHEN 'CONDOMINIO' THEN 'condomínio' " +
                     "    WHEN 'LOJA' THEN 'loja' " +
                     "    WHEN 'QUITINETE' THEN 'quitinete' " +
                     "    WHEN 'SALA_COMERCIAL' THEN 'sala comercial' " +
                     "    ELSE 'zzz' " +
                     "END ASC", nativeQuery = true)
       List<Property> findAllOrderByPropertyTypeAsc();

       @Query(value = "SELECT p.* FROM tb_properties p " +
                     "ORDER BY IF(p.number IS NULL, 1, 0), p.number ASC", nativeQuery = true)
       List<Property> findAllOrderByNumberAsc();

       @Query(value = "SELECT p.* FROM tb_properties p " +
                     "ORDER BY IF(p.payment_day IS NULL, 1, 0), CAST(p.payment_day AS SIGNED) ASC", nativeQuery = true)
       List<Property> findAllOrderByPaymentDayAsc();

       @Query(value = "SELECT p.* FROM tb_properties p " +
                     "ORDER BY IF(p.rental_value IS NULL, 1, 0), p.rental_value ASC", nativeQuery = true)
       List<Property> findAllOrderByValueAsc();
}