package at.qe.skeleton.exceptions;

import at.qe.skeleton.payload.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    @ResponseBody
    @ExceptionHandler(GameNotFoundException.class)
    ResponseEntity<?> gameNotFoundHandler(GameNotFoundException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>((new ErrorResponse(ex.getMessage(), 404)).toString(), headers, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(TeamNotFoundException.class)
    ResponseEntity<?> teamNotFoundHandler(TeamNotFoundException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>((new ErrorResponse(ex.getMessage(), 404)).toString(), headers, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(RoomNotFoundException.class)
    ResponseEntity<?> roomNotFoundHandler(RoomNotFoundException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>((new ErrorResponse(ex.getMessage(), 404)).toString(), headers, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(StatisticsNotPresentException.class)
    ResponseEntity<?> statisticsNotPresentHandler(StatisticsNotPresentException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>((new ErrorResponse(ex.getMessage(), 404)).toString(), headers, HttpStatus.NOT_FOUND);
    }
}
