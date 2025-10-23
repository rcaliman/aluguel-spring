package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import imoveis.aluguel.dtos.UserDtoRequest;
import imoveis.aluguel.dtos.UserDtoResponse;
import imoveis.aluguel.entities.User;

@Mapper(componentModel = "spring", uses = {})
public interface UserMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(UserDtoRequest dtoRequest);

    UserDtoResponse dtoResponse(User user);

}
