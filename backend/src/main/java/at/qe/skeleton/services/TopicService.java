package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.TopicNotFoundException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import java.util.List;

@Service
public class TopicService {
    public class TopicExistsException extends Exception {}

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserService userService;

    /**
     * @param topic the topic to create
     * @return newly created topic
     */
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public Topic addTopic (Topic topic) throws TopicExistsException {
        if(topicRepository.existsByName(topic.getName())){
            throw (new TopicExistsException());
        }

        Topic newTopic = new Topic();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        newTopic.setName(topic.getName());
        newTopic.setCreated_at(timestamp);

        User creator = userService.getAuthenticatedUser().get();
        newTopic.setCreator(creator);

        return topicRepository.save(newTopic);
    }


    /**
     * Updates a topic.
     *
     * @param topic the topic to update
     */
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public Topic updateTopic(Topic topic) {
        return topicRepository.save(topic);
    }


    /**
     * Find one specific topic by ID -> might want to change this to name
     * @throws TopicNotFoundException
     * @param id id of searched topic
     * @return searched topic
     */
    public Topic findTopic(Long id) throws TopicNotFoundException {
        if(!topicRepository.existsById(id)){
            throw (new TopicNotFoundException(id));
        }
        return topicRepository.getOne(id);
    }


    /**
     * Find all existing topics
     * @return all topics
     */
    public List<Topic> findAllTopics() {
        return topicRepository.findAll();
    }


    /**
     * @throws TopicNotFoundException
     * @param topic topic to be deleted
     * @return httpStatusNOCONTENT
     */
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public void deleteTopic(Topic topic){
        if(!topicRepository.existsById(topic.getId())){
            throw (new TopicNotFoundException(topic.getId()));
        }
        topicRepository.delete(topic);
    }
}