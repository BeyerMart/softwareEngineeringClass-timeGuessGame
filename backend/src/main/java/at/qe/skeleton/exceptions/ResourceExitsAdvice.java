package at.qe.skeleton.exceptions;

import at.qe.skeleton.payload.response.ErrorResponse;
import at.qe.skeleton.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ResourceExitsAdvice {
    @ResponseBody
    @ExceptionHandler(UserService.EmailExistsException.class)
    ResponseEntity<?> emailExistsHandler(UserService.EmailExistsException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>((new ErrorResponse(ex.getMessage(), 400)).toString(), headers, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(UserService.UsernameExistsException.class)
    ResponseEntity<?> usernameExistsHandler(UserService.UsernameExistsException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>((new ErrorResponse(ex.getMessage(), 400)).toString(), headers, HttpStatus.BAD_REQUEST);
    }
}
