package uz.test.abitur.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uz.test.abitur.services.NewsService;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final NewsService newsService;

    @GetMapping({"/home", "/",""})
    public String homePage(Model model) {
        model.addAttribute("news", newsService.getAll());
        return "home";
    }
}
