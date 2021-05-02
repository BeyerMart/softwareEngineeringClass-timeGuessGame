package at.qe.skeleton.tests.controller;


import at.qe.skeleton.Main;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repository.TermRepository;
import at.qe.skeleton.services.GameService;
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


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes= Main.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameControllerTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private GameService gameService;

    User testAdmin = new User("testAdmin", "password","mail@test.com");

    Topic testTopic = new Topic();

    Game testGame = new Game();

    @BeforeAll
    public void setup() {
        testAdmin.setRole(UserRole.ROLE_ADMIN);

        testTopic.setCreator(testAdmin);
        testTopic.setName("TestTopic");
        testTopic.setId(1000L);

        testGame.setName("GameControllerTestGame");
        testGame.setTopic(testTopic);
        testGame.setId(1337L);
    }

    @BeforeEach
    public void setupContext() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    public void testFindAllGames() throws Exception {
        List<Game> games = new ArrayList<Game>(Arrays.asList(testGame));
        Mockito.when(gameService.findAllGames()).thenReturn(games);

        MvcResult result = mvc.perform(get("/api/games").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testGame.getName()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    public void testFindOneGame() throws Exception {
        Mockito.when(gameService.findGame(1337L)).thenReturn(Optional.of(testGame));

        MvcResult result = mvc.perform(get("/api/games/{id}",1337L).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testGame.getName()));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testDeleteGameAsAdmin() throws Exception {
        Mockito.when(gameService.findGame(1337L)).thenReturn(Optional.of(testGame));
        mvc.perform(delete("/api/games/{gameId}",1337L)).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = "ROLE_MANAGER")
    public void testDeleteGameAsManager() throws Exception {
        Mockito.when(gameService.findGame(1337L)).thenReturn(Optional.of(testGame));
        mvc.perform(delete("/api/games/{gameId}",1337L)).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testDeleteGameAsUser() throws Exception {
        Mockito.when(gameService.findGame(1337L)).thenReturn(Optional.of(testGame));
        mvc.perform(delete("/api/games/{gameId}",1337L)).andExpect(status().isNoContent());
    }

}
