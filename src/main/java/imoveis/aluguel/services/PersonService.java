package imoveis.aluguel.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import imoveis.aluguel.entities.Person;

public interface PersonService {

    Person create(Person person);

    Person findByCpfCnpj(String cpfCnpj);

    Person findById(Long id);

    Person update(Long id, Person person);

    List<Person> list(Sort sort);

    void deleteById(Long id);

}
