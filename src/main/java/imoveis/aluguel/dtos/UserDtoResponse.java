package imoveis.aluguel.dtos;

import java.time.Instant;

import imoveis.aluguel.enums.HoleEnum;

public record UserDtoResponse(Long id, String username, String password, HoleEnum hole, Instant createdAt, Instant updatedAt) {
    
}
