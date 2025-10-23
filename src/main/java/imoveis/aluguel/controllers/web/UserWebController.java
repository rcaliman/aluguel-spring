package imoveis.aluguel.controllers.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import imoveis.aluguel.dtos.UserDtoRequest;
import imoveis.aluguel.entities.User;
import imoveis.aluguel.enums.RoleEnum;
import imoveis.aluguel.mappers.UserMapper;
import imoveis.aluguel.services.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserWebController {

    private final UserService userService;
    private final UserMapper userMapper;

    @ModelAttribute
    public User emptyUser() {
        return new User();
    }

    @GetMapping("/new")
    public String createForm(Model model) {

        model.addAttribute("user", new User());
        model.addAttribute("currentPage", "users");
        model.addAttribute("roles", RoleEnum.values());

        return "user/form";

    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {

        var user = userService.findById(id);

        model.addAttribute("user", user);
        model.addAttribute("currentPage", "users");
        model.addAttribute("roles", RoleEnum.values());

        return "user/form";

    }

    @PostMapping
    public String saveForm(@ModelAttribute UserDtoRequest dtoRequest) {

        User user = userMapper.toUser(dtoRequest);

        if (dtoRequest.id() != null) {
            userService.update(user, dtoRequest.id());
        } else {
            userService.create(user);
        }

        return "redirect:/users";

    }

    @GetMapping
    public String list(Model model) {

        var users = userService.list();

        model.addAttribute("users", users);
        model.addAttribute("currentPage", "users");

        return "user/list";

    }

}