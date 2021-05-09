package at.qe.skeleton.tests.controller;


import at.qe.skeleton.Main;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repository.GameRepository;
import at.qe.skeleton.repository.TeamRepository;
import at.qe.skeleton.services.GameService;
import at.qe.skeleton.services.TeamService;
import at.qe.skeleton.services.UserService;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Main.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TeamControllerTest {
    User testAdmin = new User("testAdmin", "password", "mail@test.com");
    User testUser = new User("testUser", "password", "mail@test.com");
    Topic testTopic = new Topic();
    Game testGame = new Game();
    Team testTeam = new Team();
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private TeamService teamService;
    @MockBean
    private GameService gameService;
    @MockBean
    private GameRepository gameRepository;
    @MockBean
    private TeamRepository teamRepository;
    @MockBean
    private UserService userService;

    @BeforeAll
    public void setup() {
        testAdmin.setRole(UserRole.ROLE_ADMIN);
        testAdmin.setId(0L);
        testUser.setRole(UserRole.ROLE_USER);

        testTopic.setCreator(testAdmin);
        testTopic.setName("TestTopic");
        testTopic.setId(1000L);

        testGame.setName("GameControllerTestGame");
        testGame.setTopic(testTopic);
        testGame.setId(1337L);

        testTeam.setId(5L);
        testTeam.setName("UptempoIsTheTempo");
        testTeam.setGame(testGame);
        testTeam.setPoints(5L);
        testTeam.setUsers(new HashSet<>());

        testGame.setTeams(Set.of(testTeam));
    }

    @BeforeEach
    public void setupContext() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        List<Game> games = new ArrayList<Game>(Arrays.asList(testGame));
        Mockito.when(gameService.findAllGames()).thenReturn(games);
        Mockito.when(gameService.findGame(testGame.getId())).thenReturn(Optional.of(testGame));
        Mockito.when(gameRepository.findById(testGame.getId())).thenReturn(Optional.of(testGame));
        Mockito.when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testUser));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testFindAllTeams() throws Exception {
        List<Team> teams = new ArrayList<Team>(Arrays.asList(testTeam));
        Mockito.when(teamService.findAllTeams()).thenReturn(teams);
        MvcResult result = mvc.perform(get("/api/teams", testGame.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testTeam.getName()));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testFindAllGameTeams() throws Exception {
        List<Team> teams = new ArrayList<Team>(Arrays.asList(testTeam));
        Mockito.when(teamService.findAllTeams()).thenReturn(teams);

        MvcResult result = mvc.perform(get("/api/games/{id}/teams", testGame.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testTeam.getName()));
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testCreateTeam() throws Exception {
        Mockito.when(teamService.addTeam(Mockito.any(Team.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        String body = "{\"name\":\"" + testTeam.getName() + "\",\"game_id\":" + testGame.getId() + "}";
        mvc.perform(post("/api/teams").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testUpdateTeam() throws Exception {
        testTeam.setUsers(new HashSet<>(Set.of(testUser)));
        Mockito.when(teamService.findTeam(testTeam.getId())).thenReturn(Optional.of(testTeam));
        Mockito.when(teamService.updateTeam(Mockito.any(Team.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        String body = "{\"name\":\"" + UUID.randomUUID().toString().substring(0, 20) + "\",\"points\":600}";
        MvcResult result = mvc.perform(patch("/api/teams/{id}", testTeam.getId()).content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("points\":5")); //Points are unmodified

        testTeam.setUsers(new HashSet<>());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testUpdateTeamWithoutMembership() throws Exception {
        Mockito.when(teamService.findTeam(testTeam.getId())).thenReturn(Optional.of(testTeam));
        Mockito.when(teamService.updateTeam(Mockito.any(Team.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        String body = "{\"name\":\"" + UUID.randomUUID().toString().substring(0, 20) + "\",\"points\":600}";
        mvc.perform(patch("/api/teams/{id}", testTeam.getId()).content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testUpdateTeamAsAdmin() throws Exception {
        Mockito.when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testAdmin));
        Mockito.when(teamService.findTeam(testTeam.getId())).thenReturn(Optional.of(testTeam));
        Mockito.when(teamService.updateTeam(Mockito.any(Team.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        String body = "{\"points\":600}";
        MvcResult result = mvc.perform(patch("/api/teams/{id}", testTeam.getId()).content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("points\":600"));
        testTeam.setPoints(5L);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testFindOneTeam() throws Exception {
        Mockito.when(teamService.findTeam(testTeam.getId())).thenReturn(Optional.of(testTeam));

        MvcResult result = mvc.perform(get("/api/teams/{id}", testTeam.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testTeam.getName()));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testGetTeamUsers() throws Exception {
        testTeam.setUsers(Set.of(testUser));
        Mockito.when(teamService.findTeam(testTeam.getId())).thenReturn(Optional.of(testTeam));
        Mockito.when(teamService.getAllTeamUsers(testTeam)).thenReturn(new ArrayList<Object>(testTeam.getUsers()));

        MvcResult result = mvc.perform(get("/api/teams/{id}/users", testTeam.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testUser.getUsername()));

        testTeam.setUsers(new HashSet<>());
    }


    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testDeleteTeam() throws Exception {
        Mockito.when(teamService.findTeam(testTeam.getId())).thenReturn(Optional.of(testTeam));
        Mockito.doAnswer((i) -> null).when(teamService).deleteTeam(testTeam);
        mvc.perform(delete("/api/teams/{id}", testTeam.getId())).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testAddUserToTeam() throws Exception {
        Mockito.when(teamService.findTeam(testTeam.getId())).thenReturn(Optional.of(testTeam));
        MvcResult result = mvc.perform(post("/api/teams/{id}/users", testTeam.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testUser.getUsername()));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testAddVirtualUserToTeam() throws Exception {
        Mockito.when(userService.getAuthenticatedUser()).thenReturn(Optional.of(testAdmin));
        Mockito.when(teamService.findTeam(testTeam.getId())).thenReturn(Optional.of(testTeam));
        Mockito.when(teamService.addVirtualUser(Mockito.any(Team.class), Mockito.any(VirtualUser.class))).thenAnswer(AdditionalAnswers.returnsSecondArg());

        String virtUserName = UUID.randomUUID().toString().substring(0, 20);

        String body = "{\"username\":\"" + virtUserName + "\"}";
        MvcResult result = mvc.perform(post("/api/teams/{id}/users", testTeam.getId()).content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(virtUserName));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testRemoveUserFromEmptyTeam() throws Exception {
        Mockito.when(teamService.findTeam(testTeam.getId())).thenReturn(Optional.of(testTeam));
        Mockito.doAnswer((i) -> null).when(teamService).removeUser(Mockito.any(Team.class), Mockito.any(User.class));
        mvc.perform(delete("/api/teams/{id}/users", testTeam.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent()).andReturn();
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testRemoveUserFromTeam() throws Exception {
        Mockito.when(teamService.findTeam(testTeam.getId())).thenReturn(Optional.of(testTeam));
        Mockito.doAnswer((i) -> null).when(teamService).removeUser(Mockito.any(Team.class), Mockito.any(User.class));
        Mockito.when(teamService.removeUser(Mockito.any(Team.class), Mockito.any(User.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        mvc.perform(delete("/api/teams/{id}/users", testTeam.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent()).andReturn();
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testRemoveVirtualUserFromTeam() throws Exception {
        Mockito.when(teamService.findTeam(testTeam.getId())).thenReturn(Optional.of(testTeam));
        VirtualUser user = new VirtualUser();
        user.setUsername(UUID.randomUUID().toString().substring(0, 20));
        user.setCreator_id(0L);

        Mockito.when(teamService.removeVirtualUser(Mockito.any(Team.class), Mockito.anyLong())).thenReturn(user);
        mvc.perform(delete("/api/teams/{id}/users/123", testTeam.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent()).andReturn();
    }
}
