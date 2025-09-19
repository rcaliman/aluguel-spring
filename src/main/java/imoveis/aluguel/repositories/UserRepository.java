package imoveis.aluguel.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import imoveis.aluguel.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    
}
