package uz.test.abitur.controller.subject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.test.abitur.dtos.subject.SubjectCreateDTO;
import uz.test.abitur.dtos.subject.SubjectDeleteDTO;
import uz.test.abitur.dtos.subject.SubjectUpdateDTO;
import uz.test.abitur.services.SubjectService;

@Controller
@RequestMapping("/subjects")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
public class SubjectController {
    private final SubjectService subjectService;
    private final ObjectMapper objectMapper;

    @GetMapping("/all")
    public String subjectListPage(Model model) {
        model.addAttribute("subjects", subjectService.getAll());
        return "subject/subjects";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute SubjectCreateDTO dto) {
        subjectService.create(dto);
        return "redirect:/subjects/all";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute SubjectUpdateDTO dto) {
        subjectService.update(dto);
        return "redirect:/subjects/all";
    }

    @PostMapping("/delete")
    public String delete(SubjectDeleteDTO dto) {
        subjectService.delete(dto.getId());
        return "redirect:/subjects/all";
    }

    @GetMapping("/get/{id:.*}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN','USER','SUPER_ADMIN')")
    public String get(@PathVariable Integer id) throws JsonProcessingException {
        return objectMapper.writeValueAsString(subjectService.getSubjectResultDTOById(id));
    }
}
