package uz.test.abitur.controller.news;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uz.test.abitur.config.security.UserSession;
import uz.test.abitur.domains.News;
import uz.test.abitur.dtos.news.NewsCreateDTO;
import uz.test.abitur.dtos.news.NewsDeleteDTO;
import uz.test.abitur.dtos.news.NewsUpdateDTO;
import uz.test.abitur.services.NewsService;

@Controller
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;
    private final ObjectMapper objectMapper;
    private final UserSession userSession;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public String create(@Valid @ModelAttribute NewsCreateDTO dto) {
        newsService.create(dto);
        return "redirect:/home";
    }
    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public String update(@Valid @ModelAttribute NewsUpdateDTO dto) {
        newsService.update(dto);
        return "redirect:/home";
    }
    @PostMapping("/delete")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public String delete(@Valid @ModelAttribute NewsDeleteDTO dto) {
        newsService.delete(dto);
        return "redirect:/home";
    }


    @GetMapping("/get/{id:.*}")
    @ResponseBody
    public String get(@PathVariable Integer id) throws JsonProcessingException {
        try {
            return objectMapper.writeValueAsString(newsService.getById(id));
        } catch (Exception e) {
            System.out.println("Message: " + e.getMessage());
        }
        return objectMapper.writeValueAsString(new News());
    }
}
