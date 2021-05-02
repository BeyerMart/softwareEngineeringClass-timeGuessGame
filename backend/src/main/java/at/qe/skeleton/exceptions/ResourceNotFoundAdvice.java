package at.qe.skeleton.exceptions;

import at.qe.skeleton.payload.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ResourceNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<?> userNotFoundHandler(UserNotFoundException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>((new ErrorResponse(ex.getMessage(), 404)).toString(), headers, HttpStatus.NOT_FOUND);
    }


    @ResponseBody
    @ExceptionHandler(TopicNotFoundException.class)
    ResponseEntity<?> topicNotFoundHandler(TopicNotFoundException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>((new ErrorResponse(ex.getMessage(), 404)).toString(), headers, HttpStatus.NOT_FOUND);
    }


    @ResponseBody
    @ExceptionHandler(TermNotFoundException.class)
    ResponseEntity<?> termNotFoundHandler(TopicNotFoundException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>((new ErrorResponse(ex.getMessage(), 404)).toString(), headers, HttpStatus.NOT_FOUND);
    }

}
