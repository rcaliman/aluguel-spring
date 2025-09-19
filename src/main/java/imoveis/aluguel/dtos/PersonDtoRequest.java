package imoveis.aluguel.dtos;

import java.time.LocalDate;
import java.util.List;

import imoveis.aluguel.enums.MaritalStatusEnum;
import imoveis.aluguel.enums.PersonTypeEnum;

public record PersonDtoRequest(Long id, String name, List<ContactDtoRequest> contacts, String document, String cpfCnpj, LocalDate dateOfBirth, String address, MaritalStatusEnum maritalStatus, PersonTypeEnum type) {

}