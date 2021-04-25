package at.qe.skeleton.tests.controller;


import at.qe.skeleton.Main;
import at.qe.skeleton.model.Term;
import at.qe.skeleton.model.Topic;
import at.qe.skeleton.model.User;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.repository.TermRepository;
import at.qe.skeleton.services.TermService;
import at.qe.skeleton.services.TopicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes= Main.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TermControllerTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private TermService termService;

    User testAdmin = new User("testAdmin", "password","mail@test.com");

    Topic testTopic = new Topic();

    Term testTerm = new Term();

    @BeforeAll
    public void setup() {
        testAdmin.setRole(UserRole.ROLE_ADMIN);

        testTopic.setCreator(testAdmin);
        testTopic.setName("TestTopic");
        testTopic.setId(1000L);

        testTerm.setName("TestTerm");
        testTerm.setTopic(testTopic);
        testTerm.setId(1337L);
    }

    @BeforeEach
    public void setupContext() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(authorities = "ROLE_MANAGER")
    public void testDeleteTermAsManager() throws Exception {
        mvc.perform(delete("/api/topics/{topicId}/terms/{termId}",1000L, 1337L)).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER"})
    public void testDeleteTermAsAdmin() throws Exception {
        mvc.perform(delete("/api/topics/{topicId}/terms/{termId}",1000L, 1337L)).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = "ROLE_MANAGER")
    public void testSearchTermAsManager() throws Exception {
        Mockito.when(termService.findTerm(1337L)).thenReturn(testTerm);

        MvcResult result = mvc.perform(get("/api/topics/{id}/terms/{termId}",1000L, 1337L).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testTerm.getName()));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testSearchTermAsUser() throws Exception {
        Mockito.when(termService.findTerm(1337L)).thenReturn(testTerm);

        MvcResult result = mvc.perform(get("/api/topics/{id}/terms/{termId}",1000L, 1337L).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testTerm.getName()));
    }

}
