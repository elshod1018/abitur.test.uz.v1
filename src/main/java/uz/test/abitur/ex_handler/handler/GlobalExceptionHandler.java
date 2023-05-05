package uz.test.abitur.ex_handler.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.test.abitur.dtos.AppErrorDTO;

@ControllerAdvice("uz.test.abitur")
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public String handleUnknownExceptions(Model model, Exception e, HttpServletRequest request) {
        e.printStackTrace();
        AppErrorDTO error = new AppErrorDTO(request.getRequestURI(), e.getMessage(), e);
        model.addAttribute("error", error);
        return "error";
    }

//    @ExceptionHandler(NotFoundException.class)
//    public String handleRuntimeExceptions(Model model, NotFoundException e, HttpServletRequest request) {
//        AppErrorDTO error = new AppErrorDTO(request.getRequestURI(), e.getMessage(), e);
//        model.addAttribute("error", error);
//        return "error";
//    }
}
