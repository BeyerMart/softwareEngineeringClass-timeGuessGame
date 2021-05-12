package at.qe.skeleton.tests.services;
import at.qe.skeleton.exceptions.GameNotFoundException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repository.GameRepository;
import at.qe.skeleton.repository.TopicRepository;
import at.qe.skeleton.repository.UserRepository;
import at.qe.skeleton.services.GameService;
import at.qe.skeleton.services.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameServiceTest {
    @MockBean
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameService gameService;

    @MockBean
    private GameRepository gameRepository;

    @MockBean
    private UserService userService;

    private Topic topic;
    private User admin;
    private Game game;

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

        game = new Game();
        game.setTopic(topic);
        game.setName("Game For Tests");
        game.setId(1000L);
        game.setTeams(new HashSet<>());

        Mockito.when(gameRepository.save(game)).thenReturn(game);
        Mockito.when(gameRepository.existsById(game.getId())).thenReturn(true);
        Mockito.when(gameRepository.getOne(game.getId())).thenReturn(game);
        Mockito.when(gameRepository.findAll()).thenReturn(Collections.singletonList(game));

        Mockito.when(userService.getAuthenticatedUser()).thenReturn(Optional.of(admin));
    }

    /**
     * TEst create game (ANY ROLE)
     * @throws Exception
     */
    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    public void testCreateGame() throws Exception {
        assertDoesNotThrow(() -> gameService.addGame(game,topic.getId()));
    }

    /**
     * Test find a specific game as admin
     */
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testFindOneGameAsAdmin() {
        assertDoesNotThrow(() -> gameService.findGame(game.getId()));
    }

    /**
     * Test find a specific game as user
     */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testFindOneGameAsUser() {
        assertDoesNotThrow(() -> gameService.findGame(game.getId()));
    }

    /**
     * Test GameNotFoundException when searching for a non-existing game
     * @throws Exception GameNotFound
     */
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testGameNotFound() throws Exception {
        assertThrows(GameNotFoundException.class,() -> gameService.findGame(123456L));
    }

    /**
     * Test find all games as ANY role
     */
    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    public void testFindAllGames() {
        assertDoesNotThrow(() -> gameService.findAllGames());
    }

    /**
     * Test update game
     */
    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    public void testUpdateGame() {
        assertDoesNotThrow(() -> gameService.updateGame(game));
    }

    /**
     * Test create game as unauthenticated
     * @throws Exception no authentication credentials
     */
    @Test
    public void testCreateAsUnauthenticated() throws Exception{
        Topic newTopic = new Topic();
        newTopic.setName(UUID.randomUUID().toString().substring(0,19));

        Game newGame = new Game();
        newGame.setName("Unauthenticated");

        assertThrows(org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class, () -> gameService.addGame(newGame, topic.getId()));
    }

    /**
     * Test delete game as user
     * @throws Exception no access
     */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testDeleteGameAsUser() throws Exception {
        assertThrows(AccessDeniedException.class,() -> gameService.deleteGame(game));
    }

    /**
     * Test delete game as admin
     */
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testDeleteGameAsAdmin() {
        assertDoesNotThrow(() -> gameService.deleteGame(game));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testBadCheckGamePoints() {
        String result = gameService.checkGamePoints(game, admin);
        assertEquals("You are not a player", result);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testCheckGamePoints() {
        Team team = new Team();
        team.setUsers(Set.of(admin));
        game.setTeams(Set.of(team));

        Team testTeam = new Team();
        testTeam.setUsers(new HashSet<>());
        gameService.addGamePoints(game, testTeam);

        String result = gameService.checkGamePoints(game, admin);
        assertNull(result);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testAddGamePoints() {
        Team testTeam = new Team();
        testTeam.setUsers(new HashSet<>());
        gameService.addGamePoints(game, testTeam);
        assertNotNull(gameService.getGamePoints(game));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testRemoveGamePoints() {
        Team testTeam = new Team();
        testTeam.setUsers(new HashSet<>());
        gameService.addGamePoints(game, testTeam);
        gameService.removeGamePoints(game);
        assertNull(gameService.getGamePoints(game));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testRejectPoints() {
        Team team = new Team();
        team.setUsers(Set.of(admin));
        game.setTeams(Set.of(team));

        Team testTeam = new Team();
        testTeam.setUsers(new HashSet<>());
        gameService.addGamePoints(game, testTeam);

        String result = gameService.rejectPoints(game);
        assertNull(result);

        result = gameService.rejectPoints(game); //Voting again shouldn't work
        assertNotNull(result);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testAcceptPoints() {
        Team team = new Team();
        team.setUsers(Set.of(admin));
        game.setTeams(Set.of(team));

        Team testTeam = new Team();
        testTeam.setUsers(new HashSet<>());
        gameService.addGamePoints(game, testTeam);

        String result = gameService.confirmPoints(game);
        assertNull(result);
        result = gameService.confirmPoints(game); //Voting again shouldn't work
        assertNotNull(result);
    }


    @AfterAll
    public void teardown() {
        userRepository.delete(admin);
    }
}