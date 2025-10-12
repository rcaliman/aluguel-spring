package imoveis.aluguel.repositories;

import imoveis.aluguel.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}