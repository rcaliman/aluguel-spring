package imoveis.aluguel.dtos;

import java.time.LocalDate;
import java.util.List;

import imoveis.aluguel.enums.MaritalStatusEnum;
import imoveis.aluguel.enums.PersonTypeEnum;

public record PersonDtoResponse(Long id, String name, List<ContactDtoResponse> contacts, String document, String cpfCnpj, LocalDate dateOfBirth, String address, MaritalStatusEnum maritalStatus, PersonTypeEnum type) {

}