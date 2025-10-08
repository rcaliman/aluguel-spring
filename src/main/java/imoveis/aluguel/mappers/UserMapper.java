package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import imoveis.aluguel.dtos.UserDtoRequest;
import imoveis.aluguel.dtos.UserDtoResponse;
import imoveis.aluguel.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toUser(UserDtoRequest dtoRequest);

    @Mapping(target = "role", ignore = true)
    UserDtoResponse dtoResponse(User user);
    
}
