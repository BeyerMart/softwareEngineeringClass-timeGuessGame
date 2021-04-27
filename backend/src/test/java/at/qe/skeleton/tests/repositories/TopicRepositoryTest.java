package at.qe.skeleton.tests.repositories;

import at.qe.skeleton.Main;
import at.qe.skeleton.model.Topic;
import at.qe.skeleton.model.User;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.repository.TopicRepository;
import at.qe.skeleton.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = Main.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TopicRepositoryTest {
    private String name;
    private Topic topic;
    private User user;
    private String username;
    private String password;
    private String email;

    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public void setup() {
        //ESSENTIAL FOR CREATOR
        username = UUID.randomUUID().toString().substring(0, 20);
        password = UUID.randomUUID().toString().substring(0, 20);
        email = UUID.randomUUID().toString().substring(0, 10) + "@" + UUID.randomUUID().toString().substring(0, 6) + ".de";

        user = new User(username,password,email);
        user.setRole(UserRole.ROLE_ADMIN);
        userRepository.save(user);

        name = UUID.randomUUID().toString().substring(0, 20);

        topic = new Topic();
        topic.setName(name);
        topic.setCreator(user);
    }

    /**
     * Test create topic
     */
    @Test
    @Order(1)
    public void testCreateTopic() throws Exception {
        assertDoesNotThrow(() -> {
            topicRepository.save(topic);
        });

        assertNotNull(topic);
    }

    /**
     * Test exists by name.
     */
    @Test
    @Order(2)
    public void testExistsByName() {
        assertTrue(topicRepository.existsByName(topic.getName()));
    }

    /**
     * Test create duplicate topic
     * @throws Exception
     */
    @Test
    @Order(2)
    public void testBadCreateTopic() throws Exception {
        Topic toCreate = new Topic();
        toCreate.setName(name);
        toCreate.setCreator(user);

        //-> same topic will be added
        assertThrows(DataIntegrityViolationException.class, () -> topicRepository.save(toCreate));
    }

    /**
     * Test find all topics.
     *
     * @throws Exception the exception
     */
    @Test
    @Order(2)
    public void testFindAllTopics() throws Exception {
        List<Topic> allTopics = topicRepository.findAll();
        List<Long> allTopicsIds = allTopics.stream().map(entry -> entry.getId()).collect(Collectors.toList());

        assertTrue(allTopicsIds.contains(topic.getId()));
    }

    /**
     * Tests modifying topic by adding new name.
     */
    @Test
    @Order(3)
    void testUpdateTopic() {
        String newName = UUID.randomUUID().toString().substring(0, 10);
        topic.setName(newName);
        topicRepository.save(topic);

        assertEquals(newName,topic.getName());
        assertNotEquals(name, topic.getName());

        //Check if changes are saved to database
        assertTrue(topicRepository.existsByName(newName));

        //undo change
        topic.setName(name);
        topicRepository.save(topic);
    }

    /**
     * Tests deleting a topic
     */
    @Test
    @Order(4)
    void testDeleteTopic() {
        topicRepository.delete(topic);
        assertFalse(topicRepository.existsByName(topic.getName()));
    }

    @AfterAll
    void teardown() {
        userRepository.delete(user);
    }
}
