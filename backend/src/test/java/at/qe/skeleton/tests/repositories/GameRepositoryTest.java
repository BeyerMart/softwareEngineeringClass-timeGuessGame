package at.qe.skeleton.tests.repositories;

import at.qe.skeleton.Main;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repository.GameRepository;
import at.qe.skeleton.repository.TopicRepository;
import at.qe.skeleton.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for GameRepository
 */
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = Main.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameRepositoryTest {
    private String gameName;
    private Game game;

    private Topic topic;

    private String username;
    private String password;
    private String email;
    private User user;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private GameRepository gameRepository;

    @BeforeAll
    public void setup() {
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

        gameName = UUID.randomUUID().toString().substring(0, 20);
        game = new Game();
        game.setName(gameName);
        game.setTopic(topic);
        gameRepository.save(game);

    }

    /**
     * Test create game
     */
    @Test
    public void testCreateGame() throws Exception {
        assertDoesNotThrow(() -> {
            gameRepository.save(game);
        });

        assertNotNull(game);
    }

    /**
     * Test Get one game
     */
    @Test
    public void testFindGame(){
        Optional<Game> searchedGame = gameRepository.findById(game.getId());
        assertNotNull(searchedGame.get());
    }

    /**
     * Test game exists by ID
     */
    @Test
    public void testExistsById() {
        assertTrue(gameRepository.existsById(game.getId()));
    }

    /**
     * Test game exists by name
     */
    @Test
    public void testExistsByName() {
        assertTrue(gameRepository.existsByName(gameName));
    }

    /**
     * Test deletion of a game
     */
    @Test
    public void testDeleteGame() {
        gameRepository.delete(game);
        assertFalse(gameRepository.existsById(game.getId()));
    }

    @AfterAll
    public void teardown() {
        topicRepository.delete(topic);
        userRepository.delete(user);
        gameRepository.delete(game);
    }
}
