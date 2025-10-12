package imoveis.aluguel.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import imoveis.aluguel.entities.Energy;

public interface EnergyRepository extends JpaRepository<Energy, Long> {

    Optional<List<Energy>> findTop3ByOrderByIdDesc();

    Optional<List<Energy>> findTop2ByOrderByIdDesc();

}
