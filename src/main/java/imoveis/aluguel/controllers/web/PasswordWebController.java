package imoveis.aluguel.controllers.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import imoveis.aluguel.entities.User;
import imoveis.aluguel.services.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/password")
@RequiredArgsConstructor
public class PasswordWebController {

    private final UserService userService;

    @ModelAttribute
    public User emptyUser() {
        return new User();
    }

    @GetMapping("/form")
    public String form(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.findByUsername(auth.getName());

        model.addAttribute("id", user.getId());

        return "password/form";

    }

    @PostMapping("/update")
    public String update(@RequestParam Long id,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {

        // Validação: verificar se as senhas coincidem
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "As senhas não coincidem. Por favor, tente novamente.");
            return "redirect:/password/form";
        }

        try {
            userService.updatePassword(id, password);
            redirectAttributes.addFlashAttribute("successMessage", "Senha alterada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao alterar senha: " + e.getMessage());
        }

        return "redirect:/";

    }

}
