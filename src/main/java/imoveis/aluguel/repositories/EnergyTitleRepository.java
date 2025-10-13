package imoveis.aluguel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import imoveis.aluguel.entities.EnergyTitle;

public interface EnergyTitleRepository extends JpaRepository<EnergyTitle, Long> {

    EnergyTitle findTopByOrderByIdDesc();

}