package at.qe.skeleton.exceptions;

import at.qe.skeleton.payload.response.ErrorResponse;
import at.qe.skeleton.services.GameService;
import at.qe.skeleton.services.TopicService;
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
    @ExceptionHandler(TopicService.TopicExistsException.class)
    ResponseEntity<?> topicExistsHandler(TopicService.TopicExistsException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>((new ErrorResponse("Topic is already taken!",400)).toString(),headers,HttpStatus.BAD_REQUEST);
    }
}
