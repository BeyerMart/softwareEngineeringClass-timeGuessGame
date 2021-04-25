package at.qe.skeleton.controller;

import at.qe.skeleton.model.*;
import at.qe.skeleton.payload.response.SuccessResponse;
import at.qe.skeleton.services.TopicService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class TopicController {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private TopicService topicService;

    @PostMapping(value = "/topics", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> createTopic(@Valid @RequestBody TopicDto newTopic) throws ParseException, TopicService.TopicExistsException {

        TopicDto topic = convertToTopicDto(topicService.addTopic(convertToTopicEntity(newTopic)));
        return new ResponseEntity<>((new SuccessResponse(topic, 201)).toString(), HttpStatus.CREATED);
    }

    @PatchMapping("topics/{id}")
    public ResponseEntity<?> updateTopic(@RequestBody Map<Object, Object> fields, @PathVariable Long id, UriComponentsBuilder uriComponentsBuilder) throws ParseException {
        Topic existingTopic = topicService.findTopic(id);

        fields.forEach((k, v) -> {
            if (!k.equals("name")) {
                Field field = ReflectionUtils.findField(Topic.class, (String) k);
                field.setAccessible(true);
                ReflectionUtils.setField(field, existingTopic, v);
            } else {
                existingTopic.setName(((String) v));
            }
        });
        TopicDto topic = convertToTopicDto(topicService.updateTopic(existingTopic));
        return ResponseEntity.ok((new SuccessResponse(topic)).toString());
    }


    @GetMapping("/topics/{id}")
    private ResponseEntity<?> searchTopic(@PathVariable Long id) {
        TopicDto topic = convertToTopicDto(topicService.findTopic(id));

        return ResponseEntity.ok((new SuccessResponse(topic)).toString());
    }

    @GetMapping(value = "/topics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllTopics() {
        List<TopicDto> topics = topicService.findAllTopics().stream().map(this::convertToTopicDto).collect(Collectors.toList());

        return ResponseEntity.ok((new SuccessResponse(topics)).toString());
    }


    @DeleteMapping("/topics/{id}")
    private ResponseEntity<?> deleteTopic(@PathVariable Long id) {
        topicService.deleteTopic(topicService.findTopic(id));

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    /**
     * Convert methods -> DTOs TO ENTITY and vice versa
     */
    private TopicDto convertToTopicDto(Topic topic) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(Topic.class, TopicDto.class).addMappings(m -> m.map(src -> src.getCreator().getId(), TopicDto::setCreatorId));
        return modelMapper.map(topic, TopicDto.class);
    }

    private Topic convertToTopicEntity(TopicDto topicDto) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(TopicDto.class, Topic.class).addMappings(m -> m.map(TopicDto::getCreatorId, Topic::setCreator));
        return modelMapper.map(topicDto, Topic.class);
    }
}