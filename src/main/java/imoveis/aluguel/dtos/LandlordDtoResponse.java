package imoveis.aluguel.dtos;

import java.time.LocalDate;
import java.util.List;

import imoveis.aluguel.enums.MaritalStatusEnum;

public record LandlordDtoResponse(Long id, String name, List<ContactDtoResponse> contacts, String document, String cpfCnpj, LocalDate dateOfBirth, String address, MaritalStatusEnum maritalStatus) {
    
}
