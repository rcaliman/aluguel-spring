package imoveis.aluguel.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import imoveis.aluguel.entities.Landlord;

public interface LandLordRepository extends JpaRepository<Landlord, Long> {
    
    Optional<Landlord> findByCpfCnpj(String cpfCnpj);

}
