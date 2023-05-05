package uz.test.abitur.controller.authuser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.test.abitur.config.security.UserSession;
import uz.test.abitur.domains.AuthUser;
import uz.test.abitur.dtos.user.UserPasswordUpdateDTO;
import uz.test.abitur.dtos.user.UserProfileUpdateDTO;
import uz.test.abitur.dtos.user.UserUpdateDTO;
import uz.test.abitur.enums.Role;
import uz.test.abitur.enums.Status;
import uz.test.abitur.ex_handler.exceptions.PasswordException;
import uz.test.abitur.services.AuthUserService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
@Slf4j
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final AuthUserService authUserService;
    private final ObjectMapper objectMapper;
    private final UserSession userSession;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public String usersPage(Model model, @RequestParam(required = false) Status status) {
        List<AuthUser> userList;
        if (!Objects.isNull(status))
            userList = authUserService.getAllByStatus(status);
        else
            userList = authUserService.getAll();
        model.addAttribute("users", userList);
        return "user/users";
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public String update(@Valid @ModelAttribute UserUpdateDTO dto) {
        authUserService.update(dto);
        return "redirect:/users/all";
    }

    @GetMapping("/get/{id:.*}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public String get(@PathVariable String id) throws JsonProcessingException {
        try {
            AuthUser user = authUserService.findById(id);
            return objectMapper.writeValueAsString(user);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return objectMapper.writeValueAsString(new AuthUser());
    }

    @GetMapping("/settings")
    public String settingsPage() {
        return "user/settings";
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        AuthUser user = userSession.getUser();
        model.addAttribute("user", user);
        return "user/profile";
    }

    @PostMapping("/profile")
    public String profileUpdate(@ModelAttribute UserProfileUpdateDTO dto) throws IOException {
        authUserService.updateProfile(dto);
        return "redirect:/users/profile";
    }

    @PostMapping("/update/password")
    public String passwordUpdate(@Valid @ModelAttribute UserPasswordUpdateDTO dto, Model model) {
        try {
            authUserService.updatePassword(dto);
        } catch (PasswordException e) {
            model.addAttribute("passwordError", e.getMessage());
            return "user/settings";
        }
        return "redirect:/users/settings";
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/admin")
    public String adminsPage(Model model, @RequestParam(required = false) String role) {
        List<AuthUser> userList;
        if (!Objects.isNull(role))
            userList = authUserService.getAllByRole(Role.valueOf(role.toUpperCase()));
        else
            userList = authUserService.getAll();
        model.addAttribute("users", userList);
        return "user/admins";
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/admin/update/promote/{userId:.*}")
    public String promoteToAdmin(@PathVariable String userId) {
        AuthUser user = authUserService.findById(userId);
        if (user.getRole().equals(Role.ADMIN))
            user.setRole(Role.USER);
        else
            user.setRole(Role.ADMIN);
        authUserService.update(user);
        return "redirect:/users/admin";
    }

}
