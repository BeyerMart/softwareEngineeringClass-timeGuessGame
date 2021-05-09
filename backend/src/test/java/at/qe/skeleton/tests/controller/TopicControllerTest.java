package at.qe.skeleton.tests.controller;


import at.qe.skeleton.Main;
import at.qe.skeleton.model.Topic;
import at.qe.skeleton.model.User;
import at.qe.skeleton.model.UserRole;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes= Main.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TopicControllerTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private TopicService topicService;

    User testAdmin = new User("testAdmin", "password","mail@test.com");

    Topic testTopic = new Topic();

    @BeforeAll
    public void setup() {
        testAdmin.setRole(UserRole.ROLE_ADMIN);

        testTopic.setCreator(testAdmin);
        testTopic.setName("TestTopic");
        testTopic.setId(1337L);
    }

    @BeforeEach
    public void setupContext() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(authorities = "ROLE_MANAGER")
    public void testCreateTopic() throws Exception {
        Mockito.when(topicService.addTopic(Mockito.any(Topic.class))).thenReturn(testTopic);

        String body = "{\"name\":\"" + testTopic.getName() + "\"}";
        mvc.perform(post("/api/topics").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isCreated()).andReturn();
    }

    @Test
    @WithMockUser(authorities = "ROLE_MANAGER")
    public void testUpdateTopic() throws Exception {
        Mockito.when(topicService.updateTopic(Mockito.any(Topic.class))).thenReturn(testTopic);
        Mockito.when(topicService.findTopic(testTopic.getId())).thenReturn(testTopic);
        String newName = UUID.randomUUID().toString().substring(0,20);
        String body = "{\"name\":\"" + newName + "\"}";

        MvcResult result = mvc.perform(patch("/api/topics/{id}", testTopic.getId()).contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(newName));
    }

    @Test
    @WithMockUser(authorities = "ROLE_MANAGER")
    public void testGetTopicsAsManager() throws Exception {
        List<Topic> topics = new ArrayList<Topic>(Arrays.asList(testTopic));
        Mockito.when(topicService.findAllTopics()).thenReturn(topics);

        MvcResult result = mvc.perform(get("/api/topics").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testTopic.getName()));
    }

    @Test
    @WithMockUser(authorities = "ROLE_MANAGER")
    public void testDeleteTopicAsManager() throws Exception {
        mvc.perform(delete("/api/topics/{id}", testTopic.getId())).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = "ROLE_MANAGER")
    public void testSearchTopicAsManager() throws Exception {
        Mockito.when(topicService.findTopic(1337L)).thenReturn(testTopic);

        MvcResult result = mvc.perform(get("/api/topics/{id}",testTopic.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testTopic.getName()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER"} )
    public void testGetTopicsAsAdmin() throws Exception {
        List<Topic> topics = new ArrayList<Topic>(Arrays.asList(testTopic));
        Mockito.when(topicService.findAllTopics()).thenReturn(topics);

        MvcResult result = mvc.perform(get("/api/topics").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testTopic.getName()));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testGetTopicsAsUser() throws Exception {
        List<Topic> topics = new ArrayList<Topic>(Arrays.asList(testTopic));
        Mockito.when(topicService.findAllTopics()).thenReturn(topics);

        MvcResult result = mvc.perform(get("/api/topics").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testTopic.getName()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER"})
    public void testDeleteTopicAsAdmin() throws Exception {
        mvc.perform(delete("/api/topics/{id}", testTopic.getId())).andExpect(status().isNoContent());
    }
}
