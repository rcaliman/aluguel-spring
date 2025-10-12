package imoveis.aluguel.repositories;

import imoveis.aluguel.entities.Landlord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LandlordRepository extends JpaRepository<Landlord, Long> {
}