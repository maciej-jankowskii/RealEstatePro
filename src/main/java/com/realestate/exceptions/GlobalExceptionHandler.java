package com.realestate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UnauthorizedException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleUnauthorizedException(UnauthorizedException ex){
        return ex.getMessage();
    }

    @ExceptionHandler({RegistrationException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleRegistrationException(RegistrationException ex){
        return ex.getMessage();
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(ResourceNotFoundException ex){
        return ex.getMessage();
    }


    @ExceptionHandler({CannotDeleteResourceException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleDeleteResourceException(CannotDeleteResourceException ex){
        return ex.getMessage();
    }
}
