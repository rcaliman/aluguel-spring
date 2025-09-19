package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import imoveis.aluguel.dtos.UserDtoRequest;
import imoveis.aluguel.dtos.UserDtoResponse;
import imoveis.aluguel.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUser(UserDtoRequest dtoRequest);
    UserDtoResponse dtoResponse(User user);
    
}
