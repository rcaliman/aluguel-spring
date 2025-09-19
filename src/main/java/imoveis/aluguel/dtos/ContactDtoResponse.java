package imoveis.aluguel.dtos;

import imoveis.aluguel.enums.ContactTypeEnum;

public record ContactDtoResponse(ContactTypeEnum type, String contact) {
    
}