package imoveis.aluguel.dtos;

import imoveis.aluguel.enums.ContactTypeEnum;

public record ContactDtoRequest(ContactTypeEnum type, String contact) {
    
}
