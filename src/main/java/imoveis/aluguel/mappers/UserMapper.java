package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import imoveis.aluguel.dtos.UserDtoRequest;
import imoveis.aluguel.dtos.UserDtoResponse;
import imoveis.aluguel.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toUser(UserDtoRequest dtoRequest);

    @Mapping(target = "role", ignore = true)
    UserDtoResponse dtoResponse(User user);
    
}
