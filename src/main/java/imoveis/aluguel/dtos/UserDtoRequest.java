package imoveis.aluguel.dtos;

import java.time.Instant;

import imoveis.aluguel.enums.RoleEnum;

public record UserDtoRequest(Long id, String username, String password, RoleEnum role, Instant createtAt, Instant updatedAt) {

}
