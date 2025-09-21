package imoveis.aluguel.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import imoveis.aluguel.entities.Person;
import imoveis.aluguel.enums.PersonTypeEnum;

public interface PersonService {

    Person create(Person person);

    Person findByCpfCnpj(String cpfCnpj);

    Person findById(Long id);

    List<Person> findByPersonType(PersonTypeEnum type);

    Person update(Long id, Person person);

    List<Person> list(Sort sort);

    void deleteById(Long id);

}
