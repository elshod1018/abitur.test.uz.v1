package uz.test.abitur.controller.authuser;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.test.abitur.domains.AuthUser;
import uz.test.abitur.domains.UserSMS;
import uz.test.abitur.dtos.user.UserRegisterDTO;
import uz.test.abitur.dtos.user.UserSMSCreateDTO;
import uz.test.abitur.enums.Status;
import uz.test.abitur.services.AuthUserService;
import uz.test.abitur.services.UserSMSService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthUserService authUserService;
    private final UserSMSService userSMSService;

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserRegisterDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(Model model, @Valid @ModelAttribute("user") UserRegisterDTO dto, BindingResult errors) {
        if (errors.hasErrors()) {
            return "auth/register";
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            errors.rejectValue("password", "", "Password does not match");
            errors.rejectValue("confirmPassword", "", "Confirm Password does not match");
            return "auth/register";
        }
        AuthUser user = authUserService.save(dto);
        userSMSService.sendSMSCode(user);
        model.addAttribute("phoneNumber", user.getPhoneNumber());
        model.addAttribute("userId", user.getId());
        return "auth/authenticate";
    }

    @PostMapping("/authenticate")
    public String authenticatePage(Model model, @ModelAttribute UserSMSCreateDTO dto) {
        String userId = dto.getUserId();
        UserSMS userSMS = userSMSService.findByUserId(userId);
        AuthUser user = authUserService.findById(userId);
        if (userSMS == null) {
            model.addAttribute("expired", true);
        } else {
            if (!userSMS.getCode().equals(dto.getCode())) {
                model.addAttribute("wrongCode", true);
            } else {
                model.addAttribute("wrongCode", false);
            }
            model.addAttribute("expired", false);
            if (userSMS.getToTime().isBefore(LocalDateTime.now())) {
                userSMS.setExpired(true);
                userSMSService.update(userSMS);
                model.addAttribute("expired", true);
            }
        }
        if (userSMS == null || !userSMS.getCode().equals(dto.getCode())) {
            model.addAttribute("phoneNumber", user.getPhoneNumber());
            model.addAttribute("userId", userId);
            return "auth/authenticate";
        }
        user.setStatus(Status.ACTIVE);
        authUserService.update(user);

        userSMS.setExpired(true);
        userSMSService.update(userSMS);
        return "redirect:/auth/login";
    }

    @GetMapping("/resend/{userId:.*}")
    public String resendCode(Model model, @PathVariable String userId) {
        AuthUser user = authUserService.findById(userId);
        if (Status.NO_ACTIVE.equals(user.getStatus())) {
            userSMSService.sendSMSCode(user);
            model.addAttribute("phoneNumber", user.getPhoneNumber());
            model.addAttribute("userId", user.getId());
            return "auth/authenticate";
        } else {
            return "redirect:/home";
        }
    }

    @GetMapping("/login")
    public String loginPage(Model model, @RequestParam(required = false) String error) {
        model.addAttribute("error", error);
        String errorMsg = "User is disabled";
        if (error != null && error.startsWith(errorMsg)) {
            String phoneNumber = "+" + (error.split(errorMsg)[1]).trim();
            AuthUser authUser = authUserService.findByPhoneNumber(phoneNumber);
            return "redirect:/auth/resend/" + authUser.getId();
        }
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "auth/logout";
    }
}
