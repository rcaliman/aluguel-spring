package imoveis.aluguel.dtos;

import java.time.LocalDate;
import java.util.List;

import imoveis.aluguel.enums.MaritalStatusEnum;

public record LandlordDtoRequest(Long id, String name, List<ContactDtoRequest> contacts, String document, String cpfCnpj, LocalDate dateOfBirth, String address, String location, String city, String state, MaritalStatusEnum maritalStatus, String nationality, Boolean main) {
    
}
