package imoveis.aluguel.services;

import java.util.List;

import imoveis.aluguel.dtos.UserDtoResponse;
import imoveis.aluguel.entities.User;

public interface UserService {

    UserDtoResponse create(User user);

    UserDtoResponse update(User user, Long id);

    UserDtoResponse findById(Long id);

    void delete(Long id);

    List<UserDtoResponse> list();

}
