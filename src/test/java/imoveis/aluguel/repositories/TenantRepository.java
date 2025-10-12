package imoveis.aluguel.repositories;

import imoveis.aluguel.entities.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
}