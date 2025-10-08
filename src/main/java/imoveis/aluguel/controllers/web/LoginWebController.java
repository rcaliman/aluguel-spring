package imoveis.aluguel.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginWebController {
    
    @GetMapping
    public String login(Model model) {

        model.addAttribute("currentPage", "login");
        
        return "login";

    }
}
