package imoveis.aluguel.dtos;

import java.time.Instant;

import imoveis.aluguel.enums.RoleEnum;

public record UserDtoResponse(Long id, String username, String password, RoleEnum role, Instant createdAt, Instant updatedAt) {

}
