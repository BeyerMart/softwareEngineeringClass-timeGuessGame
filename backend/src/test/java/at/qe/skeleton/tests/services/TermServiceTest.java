package at.qe.skeleton.tests.services;
import at.qe.skeleton.model.Term;
import at.qe.skeleton.model.Topic;
import at.qe.skeleton.model.User;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.repository.TermRepository;
import at.qe.skeleton.repository.TopicRepository;
import at.qe.skeleton.repository.UserRepository;
import at.qe.skeleton.services.TermService;
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

import java.sql.Timestamp;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TermServiceTest {
    @Autowired
    private TopicService topicService;

    @MockBean
    private TopicRepository topicRepository;

    @MockBean
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TermService termService;

    @MockBean
    private TermRepository termRepository;

    private Topic topic;
    private User admin;
    private Term term;

    @BeforeEach
    public void setUp() {
        //Create admin
        admin = new User(UUID.randomUUID().toString().substring(0,20), "passwd", UUID.randomUUID().toString().substring(0,10) + "@" + UUID.randomUUID().toString().substring(0,6) + ".de");
        admin.setRole(UserRole.ROLE_ADMIN);
        admin.setId(1337L);

        topic = new Topic();
        topic.setCreator(admin);
        topic.setName(UUID.randomUUID().toString().substring(0,20));
        topic.setId(1000L);
        topic.setUpdated_at(new Timestamp(System.currentTimeMillis()));
        topic.setCreated_at(new Timestamp(System.currentTimeMillis()));

        Mockito.when(topicRepository.save(topic)).thenReturn(topic);
        Mockito.when(topicRepository.existsById(topic.getId())).thenReturn(true);
        Mockito.when(topicRepository.existsByName(topic.getName())).thenReturn(true);
        Mockito.when(topicRepository.getOne(topic.getId())).thenReturn(topic);
        Mockito.when(topicRepository.findAll()).thenReturn(Collections.singletonList(topic));

        term = new Term();
        term.setTopic(topic);
        term.setName(UUID.randomUUID().toString().substring(0,20));
        term.setId(1000L);

        Mockito.when(termRepository.save(term)).thenReturn(term);
        Mockito.when(termRepository.existsById(term.getId())).thenReturn(true);
        Mockito.when(termRepository.getOne(term.getId())).thenReturn(term);
        Mockito.when(termRepository.findAll()).thenReturn(Collections.singletonList(term));
    }

    /**
     * Test creation of a new term as User
     * @throws AccessDeniedException
     */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testCreateNewTermAsUser() throws Exception {
        assertThrows(AccessDeniedException.class,() -> termService.addTerm(topic.getId(),term));
    }

    /**
     * Test find a specific term as admin
     */
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testFindOneTermAsAdmin() {
        assertDoesNotThrow(() -> termService.findTerm(term.getId()));
    }

    /**
     * Test find all terms as admin
     */
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testFindAllTermsAsAdmin() {
        assertDoesNotThrow(() -> termService.findAllTerms(topic));
    }

    /**
     * Test find a specific term as user
     */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testFindOneTermAsUser() {
        assertDoesNotThrow(() -> termService.findTerm(term.getId()));
    }

    /**
     * Test find all terms as user
     */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testFindAllTermsAsUser() {
        assertDoesNotThrow(() -> termService.findAllTerms(topic));
    }

    /**
     * Test deletion of a term as user
     * @throws AccessDeniedException
     */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testDeleteTermAsUser() throws Exception {
        assertThrows(AccessDeniedException.class,() -> termService.deleteTerm(term));
    }

    /**
     * Test deletion of a term as admin
     * @throws Exception
     */
    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER"})
    public void testDeleteTermAsAdmin() throws Exception {
        assertDoesNotThrow(() -> termService.deleteTerm(term));
    }

    /**
     * Test deletion of a term as manager
     * @throws Exception
     */
    @Test
    @WithMockUser(authorities = "ROLE_MANAGER")
    public void testDeleteTermAsManager() throws Exception {
        assertDoesNotThrow(() -> termService.deleteTerm(term));
    }

    @AfterAll
    public void teardown() {
        userRepository.delete(admin);
    }
}
