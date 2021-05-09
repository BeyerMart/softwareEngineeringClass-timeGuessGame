package at.qe.skeleton.tests.controller;


import at.qe.skeleton.Main;
import at.qe.skeleton.model.*;
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


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @MockBean
    private TopicService topicService;

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
    public void testCreateTerm() throws Exception {
        Mockito.when(termService.addTerm(Mockito.anyLong(), Mockito.any(Term.class))).thenReturn(testTerm);
        String body = "{\"name\":\"" + testTerm.getName() + "\",\"topic_id\":" + testTopic.getId() + "}";
        mvc.perform(post("/api/topics/{topicId}/terms",testTopic.getId()).content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "ROLE_MANAGER")
    public void testDeleteTermAsManager() throws Exception {
        mvc.perform(delete("/api/topics/{topicId}/terms/{termId}",testTopic.getId(), testTerm.getId())).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER"})
    public void testDeleteTermAsAdmin() throws Exception {
        mvc.perform(delete("/api/topics/{topicId}/terms/{termId}",testTopic.getId(), testTerm.getId())).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    public void testGetAllTerms() throws Exception {
        Mockito.when(topicService.findTopic(testTopic.getId())).thenReturn(testTopic);
        Mockito.when(termService.findAllTerms(Mockito.any(Topic.class))).thenReturn(List.of(testTerm));

        MvcResult result = mvc.perform(get("/api/topics/{topicId}/terms",testTopic.getId())).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testTerm.getName()));
    }

    @Test
    @WithMockUser(authorities = "ROLE_MANAGER")
    public void testSearchTermAsManager() throws Exception {
        Mockito.when(termService.findTerm(testTerm.getId())).thenReturn(testTerm);

        MvcResult result = mvc.perform(get("/api/topics/{id}/terms/{termId}",testTopic.getId(), testTerm.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testTerm.getName()));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testSearchTermAsUser() throws Exception {
        Mockito.when(termService.findTerm(testTerm.getId())).thenReturn(testTerm);

        MvcResult result = mvc.perform(get("/api/topics/{id}/terms/{termId}",testTopic.getId(), testTerm.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testTerm.getName()));
    }

}
