package imoveis.aluguel.dtos;

import imoveis.aluguel.enums.HoleEnum;

public record UserDtoRequest(String username, String password, HoleEnum hole) {
    
}
