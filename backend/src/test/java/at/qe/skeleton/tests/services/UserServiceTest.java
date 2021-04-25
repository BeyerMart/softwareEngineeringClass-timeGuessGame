package at.qe.skeleton.tests.services;
import at.qe.skeleton.model.User;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.repository.UserRepository;
import at.qe.skeleton.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
        User user1User = new User("test_user1", "passwd", "jonass@gmail.com");
        user1User.setRole(UserRole.ROLE_USER);
        user1User.setId(1000L);

        User managerUser = new User("lib", "passwd","lib@gmail.com");
        managerUser.setRole(UserRole.ROLE_MANAGER);


        Mockito.when(userRepository.findByUsername(user1User.getUsername())).thenReturn(Optional.of(user1User));
        Mockito.when(userRepository.findById(user1User.getId())).thenReturn(Optional.of(user1User));
        Mockito.when(userRepository.existsByEmail(user1User.getEmail())).thenReturn(true);
        Mockito.when(userRepository.existsByUsername(user1User.getUsername())).thenReturn(true);
        Mockito.when(userRepository.save(user1User)).thenReturn(user1User);

        Mockito.when(userRepository.findByUsername(managerUser.getUsername())).thenReturn(Optional.of(managerUser));
    }

    /**
    * Test that creating a admin user as manager throws an exception.
     * */
    @Test
    @WithMockUser(authorities="ROLE_MANAGER")
    public void testCreateNewAdminWithManagerAuthority() throws UserService.EmailExistsException, UserService.UsernameExistsException {
        User adminUser = new User("test_user2", "passwd","test2@gmail.com");
        adminUser.setRole(UserRole.ROLE_ADMIN);

        assertThrows(AccessDeniedException.class,() -> userService.createNewUser(adminUser));
    }

    /**
     * Test creating a admin user as admin
     * */
    @Test
    @WithMockUser(authorities="ROLE_ADMIN")
    public void testCreateNewAdminWithAdminAuthority() throws UserService.EmailExistsException, UserService.UsernameExistsException {
        User adminUser = new User("test_user2", "passwd", "test2@gmail.com");
        adminUser.setRole(UserRole.ROLE_ADMIN);

        assertDoesNotThrow(() -> userService.createNewUser(adminUser));
    }


    /**
     * Test that creating a manager or admin user as user throws an exception.
     * */
    @Test
    @WithMockUser(authorities="ROLE_USER")
    public void testCreateNewManagerOrAdminWithUserAuthority() throws UserService.EmailExistsException, UserService.UsernameExistsException {
        User managerUser =  new User("test_user2", "passwd", "test2@gmail.com");
        managerUser.setRole(UserRole.ROLE_MANAGER);

        User adminUser = new User("test_user3", "passwd", "test3@gmail.com");
        adminUser.setRole(UserRole.ROLE_ADMIN);

        assertThrows(AccessDeniedException.class,() -> userService.createNewUser(managerUser));
        assertThrows(AccessDeniedException.class,() -> userService.createNewUser(adminUser));
    }

    /**
     * Tests that a user that is not signed in can create a user. ( used for signup ).
     * */
    @Test
    public void testCreateNewUserWithNoAuthority() throws UserService.EmailExistsException, UserService.UsernameExistsException {
        User managerUser =  new User("test_user2", "passwd", "test2@gmail.com");
        managerUser.setRole(UserRole.ROLE_MANAGER);

        User adminUser = new User("test_user3", "passwd", "test3@gmail.com");
        adminUser.setRole(UserRole.ROLE_ADMIN);

        User testUser = new User("test_user4", "passwd", "test4@gmail.com");
        testUser.setRole(UserRole.ROLE_USER);

        assertThrows(AccessDeniedException.class,() -> userService.createNewUser(managerUser));
        assertThrows(AccessDeniedException.class,() -> userService.createNewUser(adminUser));
        assertDoesNotThrow(() -> userService.createNewUser(testUser));

    }

    /**
     * Tests that creating a user with existing email throws an exception
     * */
    @Test
    public void testCreateNewUserWithExistingEmail() throws UserService.EmailExistsException, UserService.UsernameExistsException {

        User testUser =  new User("test_user2", "passwd", "jonass@gmail.com");
        testUser.setRole(UserRole.ROLE_USER);

        assertThrows(UserService.EmailExistsException.class,() -> userService.createNewUser(testUser));

    }

    /**
     * Tests that user can obtain itself without being a manager.
     * */
    @Test
    @WithMockUser(authorities = "ROLE_USER", username = "test_user1")
    public void testGetOwnUser() {
        assertTrue(userService.getUserById(1000L).get().getUsername() == "test_user1");
        assertTrue(userService.getUserByUsername("test_user1").get().getUsername() == "test_user1");
    }

    /**
     * Tests that manager can obtain any user
     * */
    @Test
    @WithMockUser(authorities = "ROLE_MANAGER")
    public void testGetUserAsManager() {
        assertTrue(userService.getUserById(1000L).get().getUsername() == "test_user1");
        assertTrue(userService.getUserByUsername("test_user1").get().getUsername() == "test_user1");
    }

    /**
     * Tests that non-authenticated users can not modify users.
     * */
    @Test
    public void testModifyUserWithNoAuthority() {
        User testUser = new User("test_user1", "passwd", "jonass@gmail.com");
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userService.updateUser(testUser));
    }

    /**
     * Tests giving a user the admin role as a user throws an exception
     * */
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testModifyUserToMakeAdminAsUser() {
        User testUser = new User("test_user1", "passwd", "jonass@gmail.com");
        testUser.setRole(UserRole.ROLE_ADMIN);

        assertThrows(AccessDeniedException.class, () -> userService.updateUser(testUser));
    }

    /**
     * Tests giving a user the admin role as a manager throws an exception
     * */
    @Test
    @WithMockUser(authorities = "ROLE_MANAGER")
    public void testModifyUserToMakeAdminAsManager() {
        User testUser = new User("test_user1", "passwd", "jonass@gmail.com");
        testUser.setRole(UserRole.ROLE_ADMIN);

        assertThrows(AccessDeniedException.class, () -> userService.updateUser(testUser));
    }

    /**
     * Tests modifying a user as manager
     * */
    @Test
    @WithMockUser(authorities = "ROLE_MANAGER")
    public void testModifyUserAsManager() {
        User testUser = new User("test_user1", "passwd", "jonass@gmail.com");
        testUser.setRole(UserRole.ROLE_MANAGER);

        Mockito.when(userRepository.save(testUser)).thenReturn(testUser);
        User modifiedTestUser = userService.updateUser(testUser);
        assertEquals(testUser, modifiedTestUser);
    }

    /**
     * Tests deleting a user as manager
     * */
    @Test
    @WithMockUser(authorities = "ROLE_MANAGER", username = "lib")
    public void testDeleteUserAsManager() {
        User testUser = new User("test_user1", "passwd", "jonass@gmail.com");
        Set<UserRole> testUserRoles = new HashSet<UserRole>();
        testUser.setRole(UserRole.ROLE_USER);

        assertDoesNotThrow(() -> userService.deleteUser(testUser));
    }

    /**
     * Tests that deleting an admin user as non-admin throws an exception
    * */
    @Test
    @WithMockUser(authorities = "ROLE_MANAGER", username = "lib")
    public void testDeleteAdminAsManager() {
        User testUser = new User("test_user1", "passwd", "jonass@gmail.com");
        testUser.setRole(UserRole.ROLE_ADMIN);

        assertThrows(AccessDeniedException.class, () -> userService.deleteUser(testUser));
    }

}
