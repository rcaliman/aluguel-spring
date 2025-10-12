package imoveis.aluguel.repositories;

import imoveis.aluguel.entities.Energy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnergyRepository extends JpaRepository<Energy, Long> {

    Optional<List<Energy>> findTop2ByOrderByIdDesc();

    Optional<List<Energy>> findTop3ByOrderByIdDesc();

}