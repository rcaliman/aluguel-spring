package imoveis.aluguel.controllers.api;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import imoveis.aluguel.dtos.PersonDtoRequest;
import imoveis.aluguel.dtos.PersonDtoResponse;
import imoveis.aluguel.enums.PersonTypeEnum;
import imoveis.aluguel.mappers.PersonMapper;
import imoveis.aluguel.services.PersonService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    private final PersonMapper personMapper;

    @PostMapping
    public ResponseEntity<PersonDtoResponse> create(@RequestBody PersonDtoRequest personDtoRequest) {

        var person = personMapper.toPerson(personDtoRequest);

        var newPerson = personService.create(person);

        var dtoResponse = personMapper.toDtoResponse(newPerson);

        return new ResponseEntity<>(dtoResponse, HttpStatus.CREATED);

    }

    @GetMapping("/cpf/{cpfCnpj}")
    public ResponseEntity<PersonDtoResponse> findbyCpfCnpj(@PathVariable String cpfCnpj) {

        var person = personService.findByCpfCnpj(cpfCnpj);

        var dtoResponse = personMapper.toDtoResponse(person);

        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<List<PersonDtoResponse>> findByPersonType(
            @RequestParam(name = "type") PersonTypeEnum personType) {

        var listPerson = personService.findByPersonType(personType);
        var listDto = listPerson.stream()
                .map(personMapper::toDtoResponse)
                .toList();

        return new ResponseEntity<>(listDto, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PersonDtoResponse> findById(@PathVariable Long id) {

        var person = personService.findById(id);

        var dtoResponse = personMapper.toDtoResponse(person);

        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDtoResponse> updatePerson(@PathVariable Long id,
            @RequestBody PersonDtoRequest personDto) {

        var person = personMapper.toPerson(personDto);

        var updatedPerson = personService.update(id, person);

        var dtoResponse = personMapper.toDtoResponse(updatedPerson);

        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);

    }

    @GetMapping("/list")
    public ResponseEntity<List<PersonDtoResponse>> list(Sort sort) {

        var listPerson = personService.list(sort);

        var listDto = listPerson.stream()
                .map(personMapper::toDtoResponse)
                .toList();

        return new ResponseEntity<>(listDto, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {

        personService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
