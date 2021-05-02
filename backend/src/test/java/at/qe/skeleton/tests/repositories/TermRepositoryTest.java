package at.qe.skeleton.tests.repositories;

import at.qe.skeleton.Main;
import at.qe.skeleton.model.Term;
import at.qe.skeleton.model.Topic;
import at.qe.skeleton.model.User;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.repository.TermRepository;
import at.qe.skeleton.repository.TopicRepository;
import at.qe.skeleton.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = Main.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TermRepositoryTest {
    private String termName;

    private Term term;

    private Topic topic;

    private User user;

    private String username;

    private String password;

    private String email;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TermRepository termRepository;

    @BeforeAll
    public void setup() {
        //ESSENTIAL FOR CREATOR in TOPIC
        username = UUID.randomUUID().toString().substring(0, 20);
        password = UUID.randomUUID().toString().substring(0, 20);
        email = UUID.randomUUID().toString().substring(0, 10) + "@" + UUID.randomUUID().toString().substring(0, 6) + ".de";

        user = new User(username,password,email);
        user.setRole(UserRole.ROLE_ADMIN);
        userRepository.save(user);

        topic = new Topic();
        topic.setName(UUID.randomUUID().toString().substring(0, 20));
        topic.setCreator(user);
        topicRepository.save(topic);

        termName = UUID.randomUUID().toString().substring(0, 20);
    }

    /**
     * Test create term
     */
    @Test
    @Order(1)
    public void testCreateTerm()  {
        term = new Term();
        term.setName(termName);
        term.setTopic(topic);
        termRepository.save(term);

        assertNotNull(term);
    }

    /**
     * Test find all terms by topic.
     */
    @Test
    @Order(2)
    public void testFindAllTerms()  {
        List<Term> allTerms = termRepository.findByTopic(topic);
        List<Long> allTermsIds = allTerms.stream().map(entry -> entry.getId()).collect(Collectors.toList());

        assertTrue(allTermsIds.contains(term.getId()));
    }

    /**
     * Test find one specific term
     */
    @Test
    @Order(2)
    public void testGetOne(){
        Long searchedTerm = termRepository.getOne(term.getId()).getId();
        assertEquals(searchedTerm, term.getId());
    }

    /**
     * Test exists by id
     */
    @Test
    @Order(2)
    public void testExistsById() {
        assertTrue(termRepository.existsById(term.getId()));
    }

    /**
     * Tests deleting a term
     */
    @Test
    @Order(3)
    public void testDeleteTerm() {
        termRepository.delete(term);
        assertFalse(termRepository.existsById(term.getId()));
    }

    @AfterAll
    public void teardown() {
        topicRepository.delete(topic);
        userRepository.delete(user);
    }
}