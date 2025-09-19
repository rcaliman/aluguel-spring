package imoveis.aluguel.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import imoveis.aluguel.dtos.UserDtoRequest;
import imoveis.aluguel.dtos.UserDtoResponse;
import imoveis.aluguel.mappers.UserMapper;
import imoveis.aluguel.services.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    final private UserService userService;
    final private UserMapper userMapper;

    @PostMapping("/new")
    public ResponseEntity<UserDtoResponse> saveUser(@RequestBody UserDtoRequest dtoRequest) {

        var user = userMapper.toUser(dtoRequest);

        var savedUser = userService.saveUser(user);

        var dtoResponse = userMapper.dtoResponse(savedUser);

        return new ResponseEntity<>(dtoResponse, HttpStatus.CREATED);

    }
}
