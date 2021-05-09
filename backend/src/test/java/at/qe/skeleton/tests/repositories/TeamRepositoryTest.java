package at.qe.skeleton.tests.repositories;

import at.qe.skeleton.Main;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repository.GameRepository;
import at.qe.skeleton.repository.TeamRepository;
import at.qe.skeleton.repository.TopicRepository;
import at.qe.skeleton.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Team Repository
 */
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = Main.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TeamRepositoryTest {
    private String teamName;
    private long points;

    private Team team;
    private Team team2;

    private Game game;
    private Game game2;
    private Topic topic;
    private User user;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public void setup() {
        user = new User(UUID.randomUUID().toString().substring(0, 20),UUID.randomUUID().toString().substring(0, 20),UUID.randomUUID().toString().substring(0, 10) + "@" + UUID.randomUUID().toString().substring(0, 6) + ".de");
        user.setRole(UserRole.ROLE_ADMIN);
        userRepository.save(user);

        teamName = UUID.randomUUID().toString().substring(0, 20);
        points = ThreadLocalRandom.current().nextInt(0, 10000);

        topic = new Topic();
        topic.setName(UUID.randomUUID().toString().substring(0, 20));
        topic.setCreator(user);
        topicRepository.save(topic);

        game = new Game();
        game.setName(UUID.randomUUID().toString().substring(0, 20));
        game.setTopic(topic);
        gameRepository.save(game);

        game2 = new Game();
        game2.setName(UUID.randomUUID().toString().substring(0, 19));
        game2.setTopic(topic);
        gameRepository.save(game2);
    }

    /**
     * Test create team
     */
    @Test
    @Order(1)
    public void testCreateTeam() throws Exception {
        Team toCreate = new Team();
        toCreate.setName(teamName);
        toCreate.setPoints(points);
        toCreate.setGame(game);

        assertDoesNotThrow(() -> {
            team = teamRepository.save(toCreate);
        });

        assertNotNull(team);
    }

    @Test
    @Order(2)
    public void testCreateTeamDiffGame() throws Exception {
        Team toCreate = new Team();
        toCreate.setName(teamName);
        toCreate.setPoints(points);
        toCreate.setGame(game2);

        //Duplicate entries
        assertDoesNotThrow(() -> {
            team2 = teamRepository.save(toCreate);
        });

        assertNotNull(team2);
    }

    /**
     * Test find by id.
     *
     * @throws Exception the exception
     */
    @Test
    @Order(2)
    public void testFindById() throws Exception {
        Optional<Team> foundTeam = teamRepository.findById(team.getId());
        assertEquals(team.getId(), foundTeam.get().getId());
    }

    /**
     * Test exists by name.
     */
    @Test
    @Order(2)
    public void testExistsByName() {
        assertTrue(teamRepository.existsByName(team.getName()));
    }

    /**
     * Test doesn't exist by name.
     */
    @Test
    @Order(2)
    public void testDoesntExistsByName() {
        assertFalse(teamRepository.existsByName("abc"));
    }

    /**
     * Test exists by id.
     */
    @Test
    @Order(2)
    public void testExistsById() {
        assertTrue(teamRepository.existsById(team.getId()));
    }

    /**
     * Test find all teams.
     *
     * @throws Exception the exception
     */
    @Test
    @Order(2)
    public void testFindAllTeams() throws Exception {
        List<Team> allTeams = teamRepository.findAll();
        List<Long> allTeamIds = allTeams.stream().map(entry -> entry.getId()).collect(Collectors.toList());

        assertTrue(allTeamIds.contains(team.getId()));
    }

    /**
     * Tests modifying team by changing name
     */
    @Test
    @Order(3)
    void testModifyTeam() {
        String newName = UUID.randomUUID().toString().substring(0, 20);
        team.setName(newName);
        teamRepository.save(team);

        //Check if data is saved to db
        assertTrue(teamRepository.existsByName(newName));

        team.setName(teamName);
        teamRepository.save(team);
    }

    /**
     * Test add a player
     */
    @Test
    @Order(3)
    void testAddPlayer() {
        Team dbTeam = teamRepository.findById(team.getId()).get();
        List<Long> allUserIds = dbTeam.getUsers().stream().map(entry -> entry.getId()).collect(Collectors.toList());
        assertFalse(allUserIds.contains(user.getId()));

        dbTeam.getUsers().add(user);
        teamRepository.save(dbTeam);

        //Refresh data
        dbTeam = teamRepository.findById(team.getId()).get();
        allUserIds = dbTeam.getUsers().stream().map(entry -> entry.getId()).collect(Collectors.toList());

        assertTrue(allUserIds.contains(user.getId()));
    }

    /**
     * Test remove a player
     */
    @Test
    @Order(4)
    void testRemovePlayer() {
        Team dbTeam = teamRepository.findById(team.getId()).get();
        List<Long> allUserIds = dbTeam.getUsers().stream().map(entry -> entry.getId()).collect(Collectors.toList());
        assertTrue(allUserIds.contains(user.getId()));

        dbTeam.setUsers(Set.of());
        teamRepository.save(dbTeam);

        //Refresh data
        dbTeam = teamRepository.findById(team.getId()).get();

        assertTrue(dbTeam.getUsers().size() == 0);
    }

    /**
     * Tests deleting a team
     */
    @Test
    @Order(5)
    void testDeleteTeam() {
        teamRepository.delete(team);
        assertTrue(teamRepository.existsByName(teamName)); //One team still exists

        teamRepository.delete(team2);
        assertFalse(teamRepository.existsByName(teamName));
    }

    @AfterAll
    public void teardown() {
        gameRepository.delete(game);
        gameRepository.delete(game2);
        topicRepository.delete(topic);
        userRepository.delete(user);
    }
}
