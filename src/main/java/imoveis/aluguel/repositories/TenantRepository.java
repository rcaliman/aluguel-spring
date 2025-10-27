package imoveis.aluguel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import imoveis.aluguel.entities.Tenant;

import java.util.List;
import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByCpfCnpj(String cpfCnpj);

    @Query("SELECT DISTINCT t FROM Tenant t LEFT JOIN FETCH t.properties ORDER BY t.name ASC")
    List<Tenant> findAllWithProperties();

}
