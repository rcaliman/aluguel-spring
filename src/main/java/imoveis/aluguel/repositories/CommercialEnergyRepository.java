package imoveis.aluguel.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import imoveis.aluguel.entities.CommercialEnergy;

public interface CommercialEnergyRepository extends JpaRepository<CommercialEnergy, Long> {

    Optional<List<CommercialEnergy>> findTop3ByOrderByIdDesc();

    Optional<List<CommercialEnergy>> findTop2ByOrderByIdDesc();

    Optional<CommercialEnergy> findTopByOrderByIdDesc();

}
