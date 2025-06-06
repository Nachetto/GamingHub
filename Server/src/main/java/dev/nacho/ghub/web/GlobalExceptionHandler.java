package dev.nacho.ghub.web;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {
    /*

    @ExceptionHandler(UsernameDuplicatedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public String handleUsernameDuplicated(UsernameDuplicatedException e) {
        return e.getMessage();
    }

     */
}
