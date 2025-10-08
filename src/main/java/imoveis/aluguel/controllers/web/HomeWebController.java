package imoveis.aluguel.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeWebController {

    @GetMapping
    public String home(Model model) {

        model.addAttribute("currentPage", "home");

        return "home";
        
    }
}
