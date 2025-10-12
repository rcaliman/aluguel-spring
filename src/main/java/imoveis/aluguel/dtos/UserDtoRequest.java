package imoveis.aluguel.dtos;

import imoveis.aluguel.enums.RoleEnum;

public record UserDtoRequest(String username, String password, RoleEnum role) {

}
