package at.qe.skeleton.tests.services;
import at.qe.skeleton.model.Topic;
import at.qe.skeleton.model.User;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.repository.TopicRepository;
import at.qe.skeleton.repository.UserRepository;
import at.qe.skeleton.services.TopicService;
import at.qe.skeleton.services.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TopicServiceTest {
    @Autowired
    private TopicService topicService;

    @MockBean
    private TopicRepository topicRepository;

    @MockBean
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private Topic topic;
    private User admin;

    @BeforeEach
    public void setUp() {
        //Create admin
        admin = new User(UUID.randomUUID().toString().substring(0,20), "passwd", UUID.randomUUID().toString().substring(0,10) + "@" + UUID.randomUUID().toString().substring(0,6) + ".de");
        admin.setRole(UserRole.ROLE_ADMIN);

        topic = new Topic();
        topic.setCreator(admin);
        topic.setName(UUID.randomUUID().toString().substring(0,20));
        topic.setId(1000L);
        topic.setTerms(new HashSet<>());

        Mockito.when(topicRepository.save(topic)).thenReturn(topic);
        Mockito.when(topicRepository.existsById(topic.getId())).thenReturn(true);
        Mockito.when(topicRepository.existsByName(topic.getName())).thenReturn(true);
        Mockito.when(topicRepository.findById(topic.getId())).thenReturn(Optional.of(topic));
        Mockito.when(topicRepository.findAll()).thenReturn(Collections.singletonList(topic));
    }

    /**
     * Test creation of a new topic as User
     * @throws AccessDeniedException
     */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testCreateNewTopicAsUser() throws Exception {
        assertThrows(AccessDeniedException.class,() -> topicService.addTopic(topic));
    }

    /**
     * Test find a specific topic as admin
     */
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testFindOneTopicAsAdmin() {
        assertDoesNotThrow(() -> topicService.findTopic(topic.getId()));
    }

    /**
     * Test find all topics as admin
     */
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testFindAllTopicsAsAdmin() {
        assertDoesNotThrow(() -> topicService.findAllTopics());
    }

    /**
     * Test find a specific topic as user
     */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testFindOneTopicAsUser() {
        assertDoesNotThrow(() -> topicService.findTopic(topic.getId()));
    }

    /**
     * Test find all topics as user
     */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testFindAllTopicsAsUser() {
        assertDoesNotThrow(() -> topicService.findAllTopics());
    }

    /**
     * Test deletion of a topic as user
     * @throws AccessDeniedException
     */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testDeleteTopicAsUser() throws Exception {
        assertThrows(AccessDeniedException.class,() -> topicService.deleteTopic(topic));
    }

    /**
     * Test deletion of a topic as admin
     * @throws Exception
     */
    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER"})
    public void testDeleteTopicAsAdmin() throws Exception {
        assertDoesNotThrow(() -> topicService.deleteTopic(topic));
    }

    /**
     * Test deletion of a topic as manager
     * @throws Exception
     */
    @Test
    @WithMockUser(authorities = "ROLE_MANAGER")
    public void testDeleteTopicAsManager() throws Exception {
        assertDoesNotThrow(() -> topicService.deleteTopic(topic));
    }

    /**
     * Test creation of a topic as manager
     */
    @Test
    @WithMockUser(authorities = "ROLE_MANAGER")
    public void testCreateAsManager() {
        Topic newTopic = new Topic();
        newTopic.setName(UUID.randomUUID().toString().substring(0,19));

        //Simulate authentication
        admin.setRole(UserRole.ROLE_MANAGER);
        Mockito.when(userService.getAuthenticatedUser()).thenReturn(Optional.of(admin));

        assertDoesNotThrow(() -> topicService.addTopic(newTopic));
    }

    /**
     * Test creation of a new topic as admin
     */
    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER"})
    public void testCreateAsAdmin() {
        Topic newTopic = new Topic();
        newTopic.setName(UUID.randomUUID().toString().substring(0,19));

        //Simulate authentication
        Mockito.when(userService.getAuthenticatedUser()).thenReturn(Optional.of(admin));

        assertDoesNotThrow(() -> topicService.addTopic(newTopic));
    }

    /**
     * Test creation of a topic as non-authenticated
     */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testCreateAsUnauthenticated() {
        Topic newTopic = new Topic();
        newTopic.setName(UUID.randomUUID().toString().substring(0,19));

        assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> topicService.addTopic(newTopic));
    }


    @AfterAll
    public void teardown() {
        userRepository.delete(admin);
    }
}
