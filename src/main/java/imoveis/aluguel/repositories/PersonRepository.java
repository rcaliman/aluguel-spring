package imoveis.aluguel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import imoveis.aluguel.entities.Person;
import imoveis.aluguel.enums.PersonTypeEnum;

import java.util.List;
import java.util.Optional;


public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByCpfCnpj(String cpfCnpj);
    Optional<List<Person>> findByType(PersonTypeEnum type);

}
