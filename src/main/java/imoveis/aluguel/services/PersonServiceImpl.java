package imoveis.aluguel.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import imoveis.aluguel.entities.Person;
import imoveis.aluguel.enums.PersonTypeEnum;
import imoveis.aluguel.mappers.PersonMapper;
import imoveis.aluguel.repositories.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    @Override
    @Transactional
    public Person create(Person person) {


        if(person.getContacts() != null) {
            person.getContacts().forEach(
                contact -> contact.setPerson(person)
            );
        }
        
        return personRepository.save(person);

    }

    @Override
    public Person findByCpfCnpj(String cpfCnpj) {

        var person = personRepository.findByCpfCnpj(cpfCnpj).orElseThrow(
            () -> new EntityNotFoundException(String.format("CPF/CNPJ %d não encontrado", cpfCnpj))
        );

        return person;

    }

    @Override
    public Person findById(Long id) {

        var person = personRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException(String.format("Pessoa de id %d não encontrada", id))
        );

        return person;
        
    }
    
    @Override
    @Transactional
    public Person update(Long id, Person updatedPerson) {

        Person person = personRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException(String.format("Pessoa de id %d não encontrada", id))
        );

        personMapper.updateEntity(updatedPerson, person);

        person.getContacts().clear();

        if(updatedPerson.getContacts() != null) {
            updatedPerson.getContacts().forEach(
                contact -> {
                    contact.setPerson(person);
                    person.getContacts().add(contact);
                }
            );
        }

        return personRepository.save(person);

    }

    @Override
    public List<Person> list(Sort sort) {
        return personRepository.findAll(sort);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        var person = personRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException(String.format("Pessoa de id %d não encontrada.", id))
        );
        personRepository.delete(person);
    }

    @Override
    public List<Person> findByPersonType(PersonTypeEnum type) {
        return personRepository.findByType(type).orElseThrow(
            () -> new EntityNotFoundException(String.format("Nenhum %s encontrado.", type))
        );
    }

}
