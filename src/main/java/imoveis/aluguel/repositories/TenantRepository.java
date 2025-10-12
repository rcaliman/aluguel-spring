package imoveis.aluguel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import imoveis.aluguel.entities.Tenant;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByCpfCnpj(String cpfCnpj);

}
