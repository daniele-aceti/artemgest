package artemgest.artemgest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GenericException {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAllExceptions(Throwable ex, Model model) {
        model.addAttribute("message", "Si è verificato un errore imprevisto.");
        model.addAttribute("errorDetails", ex.getMessage());
        return "/error/error";  // error.html da templates
    }
}
