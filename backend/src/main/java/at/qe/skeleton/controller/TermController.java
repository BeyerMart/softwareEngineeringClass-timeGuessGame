package at.qe.skeleton.controller;

import at.qe.skeleton.model.Term;
import at.qe.skeleton.model.TermDto;
import at.qe.skeleton.model.Topic;
import at.qe.skeleton.payload.response.SuccessResponse;
import at.qe.skeleton.services.TermService;
import at.qe.skeleton.services.TopicService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class TermController {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private TermService termService;
    @Autowired
    private TopicService topicService;

    @PostMapping(value = "/topics/{id}/terms", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> createTerm(@PathVariable Long id, @Valid @RequestBody TermDto newTerm) {
        TermDto term = convertToTermDto(termService.addTerm(id, convertToTermEntity(newTerm)));
        return new ResponseEntity<>((new SuccessResponse(term, 201)).toString(), HttpStatus.CREATED);
    }


    @GetMapping("/topics/{id}/terms/{termId}")
    private ResponseEntity<?> searchTerm(@PathVariable Long id, @PathVariable Long termId) {
        //TopicDto topic = topicService.findTopic(id).map(this::convertToDto);
        TermDto term = convertToTermDto(termService.findTerm(termId));

        return ResponseEntity.ok((new SuccessResponse(term)).toString());
    }

    @GetMapping(value = "/topics/{topicId}/terms", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllTerms(@PathVariable Long topicId) {
        Topic topic = topicService.findTopic(topicId);
        List<TermDto> terms = termService.findAllTerms(topic).stream().map(this::convertToTermDto).collect(Collectors.toList());
        return ResponseEntity.ok((new SuccessResponse(terms)).toString());
    }

    @DeleteMapping("/topics/{topicId}/terms/{termId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private ResponseEntity<?> deleteTerm(@PathVariable Long topicId, @PathVariable Long termId) {
        termService.deleteTerm(termService.findTerm(termId));

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    /**
     * Convert methods -> DTOs TO ENTITY and vice versa
     */
    private TermDto convertToTermDto(Term term) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(Term.class, TermDto.class).addMappings(m -> m.map(src -> src.getTopic().getId(), TermDto::setTopicId));
        return modelMapper.map(term, TermDto.class);
    }

    private Term convertToTermEntity(TermDto termDto) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(TermDto.class, Term.class).addMappings(m -> m.map(TermDto::getTopicId, Term::setTopic));
        return modelMapper.map(termDto, Term.class);
    }
}


