package at.qe.skeleton.tests.controller;


import at.qe.skeleton.Main;
import at.qe.skeleton.model.*;
import at.qe.skeleton.services.GameService;
import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.TeamService;
import at.qe.skeleton.services.TopicService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
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

    @MockBean
    private TopicService topicService;

    @MockBean
    private TeamService teamService;


    User testAdmin = new User("testAdmin", "password","mail@test.com");

    Topic testTopic = new Topic();

    Game testGame = new Game();

    @MockBean
    private RoomService roomService;

    @BeforeAll
    public void setup() {
        testAdmin.setRole(UserRole.ROLE_ADMIN);

        testTopic.setCreator(testAdmin);
        testTopic.setName("TestTopic");
        testTopic.setId(1000L);

        testGame.setName(UUID.randomUUID().toString().substring(0,20));
        testGame.setTopic(testTopic);
        testGame.setId(1337L);
        testGame.setTeams(new HashSet<>());
    }

    @BeforeEach
    public void setupContext() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }


    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    public void testCreateGame() throws Exception {
        Mockito.when(teamService.addTeam(Mockito.any(Team.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        Mockito.doAnswer((i) -> null).when(teamService).addVirtualUser(Mockito.any(Team.class),Mockito.any(VirtualUser.class));

        Room room = new Room(0, -1);
        room.setName("Hell yeah");
        room.setTopic_id(testTopic.getId());
        room.setCube(new Cube());

        Map<String, VirtualTeam> teamMap = new HashMap<>();
        VirtualTeam a = new VirtualTeam();
        VirtualTeam b = new VirtualTeam();

        //Construct virtual users
        Set<UserIdVirtualUser> users = new HashSet<>();
        users.add(new UserIdVirtualUser(1L));
        users.add(new UserIdVirtualUser(2L));

        a.setPlayers(users);
        b.setPlayers(users);
        teamMap.put("a", a);
        teamMap.put("b", b);
        room.setTeams(teamMap);

        Mockito.when(roomService.getRoomById(room.getId())).thenReturn(Optional.of(room));
        Mockito.when(gameService.addGame(Mockito.any(Game.class), Mockito.any())).thenReturn(testGame);

        String body = "{\"room_id\":" + room.getId() + "}";
        MvcResult result = mvc.perform(post("/api/games").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testGame.getName()));
    }


    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    public void testFindAllGames() throws Exception {
        List<Game> games = new ArrayList<>(Arrays.asList(testGame));
        Mockito.when(gameService.findAllGames()).thenReturn(games);

        MvcResult result = mvc.perform(get("/api/games").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testGame.getName()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN","MANAGER", "USER"})
    public void testUpdateGameTopic() throws Exception {
        String body = "{\"topic_id\":" + testTopic.getId() + "}";
        Mockito.when(gameService.findGame(testGame.getId())).thenReturn(Optional.of(testGame));
        Mockito.when(topicService.findTopic(testTopic.getId())).thenReturn(testTopic);
        Mockito.when(gameService.updateGame(Mockito.any(Game.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        MvcResult result = mvc.perform(patch("/api/games/{gameId}", testGame.getId()).content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("topic_id\":" + testTopic.getId()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    public void testFindOneGame() throws Exception {
        Mockito.when(gameService.findGame(testGame.getId())).thenReturn(Optional.of(testGame));

        MvcResult result = mvc.perform(get("/api/games/{id}",testGame.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testGame.getName()));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testDeleteGameAsAdmin() throws Exception {
        Mockito.when(gameService.findGame(testGame.getId())).thenReturn(Optional.of(testGame));
        mvc.perform(delete("/api/games/{gameId}",testGame.getId())).andExpect(status().isNoContent()).andReturn();
    }

    @Test
    @WithMockUser(authorities = "ROLE_MANAGER")
    public void testDeleteGameAsManager() throws Exception {
        Mockito.when(gameService.findGame(testGame.getId())).thenReturn(Optional.of(testGame));
        mvc.perform(delete("/api/games/{gameId}",testGame.getId())).andExpect(status().isNoContent()).andReturn();
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testDeleteGameAsUser() throws Exception {
        Mockito.when(gameService.findGame(testGame.getId())).thenReturn(Optional.of(testGame));
        mvc.perform(delete("/api/games/{gameId}",testGame.getId())).andExpect(status().isNoContent()).andReturn();
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testGetGamesByTopic() throws Exception {
        Mockito.when(gameService.findAllGamesByTopic(Mockito.any(Topic.class))).thenReturn(List.of(testGame));
        Mockito.when(topicService.findTopic(testTopic.getId())).thenReturn(testTopic);

        MvcResult result = mvc.perform(get("/api/topics/{topicId}/games",testTopic.getId())).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testGame.getName()));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testGetAllTeams() throws Exception {
        Mockito.when(gameService.findGame(testGame.getId())).thenReturn(Optional.of(testGame));

        MvcResult result = mvc.perform(get("/api/games/{gameId}/teams",testGame.getId())).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("data\":[]"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testAcceptPoints() throws Exception {
        Mockito.when(gameService.findGame(testGame.getId())).thenReturn(Optional.of(testGame));
        Mockito.when(gameService.confirmPoints(testGame)).thenReturn(null);
        mvc.perform(post("/api/games/{gameId}/points",testGame.getId())).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testRejectPoints() throws Exception {
        Mockito.when(gameService.findGame(testGame.getId())).thenReturn(Optional.of(testGame));
        Mockito.when(gameService.confirmPoints(testGame)).thenReturn(null);
        mvc.perform(put("/api/games/{gameId}/points",testGame.getId())).andExpect(status().isOk());
    }

}
