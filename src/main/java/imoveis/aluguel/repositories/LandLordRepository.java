package imoveis.aluguel.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import imoveis.aluguel.entities.Landlord;

public interface LandLordRepository extends JpaRepository<Landlord, Long> {

    Optional<Landlord> findByCpfCnpj(String cpfCnpj);

    @Modifying
    @Query("UPDATE Landlord l SET l.main = false")
    void setAllMainToFalse();

}
