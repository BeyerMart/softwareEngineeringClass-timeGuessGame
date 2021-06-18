package at.qe.skeleton.tests.services;

import at.qe.skeleton.exceptions.TeamNotFoundException;
import at.qe.skeleton.model.Team;
import at.qe.skeleton.model.User;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.model.VirtualUser;
import at.qe.skeleton.repository.TeamRepository;
import at.qe.skeleton.services.TeamService;
import at.qe.skeleton.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TeamServiceTest {
    @Autowired
    private TeamService teamService;

    @MockBean
    private TeamRepository teamRepository;

    @MockBean
    private UserService userService;

    private Team team;
    private User user;

    @BeforeEach
    public void setUp() {
        team = new Team();
        team.setId(0L);
        team.setName(UUID.randomUUID().toString().substring(0, 20));
        team.setPoints(0L);
        team.setGame(null);
        team.setUsers(new HashSet<>());

        user = new User(UUID.randomUUID().toString().substring(0, 20), UUID.randomUUID().toString().substring(0, 20), UUID.randomUUID().toString().substring(0, 10) + "@" + UUID.randomUUID().toString().substring(0, 6) + ".de");
        user.setId(0L);
        user.setRole(UserRole.ROLE_USER);

        Mockito.when(teamRepository.save(team)).thenReturn(team);
        Mockito.when(teamRepository.existsById(team.getId())).thenReturn(true);
        Mockito.when(teamRepository.getOne(team.getId())).thenReturn(team);
        Mockito.when(teamRepository.findAll()).thenReturn(List.of(team));

        Mockito.when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));
    }

    /**
     * Test creation of a new team as User
     */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testCreateNewTeamAsUser() {
        assertDoesNotThrow(() -> teamService.addTeam(team));
    }

    /**
     * Test assignment of user to team
     */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testAddUser() {
        Team result = teamService.addUser(team, user);
        assertTrue(result.getUsers().contains(user));
    }

    /**
     * Test get all users
     */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testGetAllTeamUsers() {
        team.setUsers(Set.of(user));
        ArrayList<Object> users = teamService.getAllTeamUsers(team);
        assertTrue(users.contains(user));
    }

    /**
     * Test add virtual user
     */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testAddVirtualUser() {
        VirtualUser virtualUser = new VirtualUser();
        virtualUser.setUsername("heyx");
        virtualUser.setCreator_id(0L);
        VirtualUser result = teamService.addVirtualUser(team, virtualUser);

        ArrayList<Object> users = teamService.getAllTeamUsers(team);
        assertTrue(users.contains(result));
    }

    /**
     * Test remove bad virtual user
     */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testRemoveBadVirtualUser() {
        VirtualUser virtualUser = new VirtualUser();
        virtualUser.setUsername("heyx");
        virtualUser.setCreator_id(1L); //BAD ID
        VirtualUser result = teamService.addVirtualUser(team, virtualUser);

        //Delete them
        assertThrows(AccessDeniedException.class, () -> teamService.removeVirtualUser(team, result.getVirtual_id()));
    }

    /**
     * Test remove virtual user
     */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testRemoveVirtualUser() {
        VirtualUser virtualUser = new VirtualUser();
        virtualUser.setUsername("heyx");
        virtualUser.setCreator_id(0L);
        VirtualUser result = teamService.addVirtualUser(team, virtualUser);

        //Delete them
        assertDoesNotThrow(() -> teamService.removeVirtualUser(team, result.getVirtual_id()));
    }

    /**
     * Test removal of user to team relation
     */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testRemoveUser() {
        team.getUsers().add(user);

        Team result;
        result = teamService.removeUser(team, user);
        assertNotNull(result); //null because team should be deleted
    }

    /**
     * Test team update
     */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testTeamUpdate() {
        team.setName("abc");
        Team result = teamService.updateTeam(team);
        assertEquals("abc", result.getName());
    }

    /**
     * Test find team by id
     */
    @Test
    public void testFindTeamById() {
        assertThrows(TeamNotFoundException.class, () -> {
            teamService.findTeam((long) -1);
        });

        assertNotNull(teamService.findTeam(0L));
    }

    /**
     * Test find all teams
     */
    @Test
    public void testFindAllTeams() {
        List<Team> teams = teamService.findAllTeams();
        assertTrue(teams.contains(team));
    }

    /**
     * Test delete teams
     */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testDeleteTeam() {
        assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> teamService.deleteTeam(team));
    }

    /**
     * Test delete teams as admin
     */
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testDeleteTeamAsAdmin() {
        assertDoesNotThrow(() -> teamService.deleteTeam(team));
    }

    /**
     * Test delete bad teams
     */
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testDeleteBadTeam() {
        Team newTeam = new Team();
        newTeam.setId((long) -1);

        assertThrows(TeamNotFoundException.class, () -> teamService.deleteTeam(newTeam));
    }
}
