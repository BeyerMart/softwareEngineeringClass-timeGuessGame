package at.qe.skeleton.tests.repositories;

import at.qe.skeleton.Main;
import at.qe.skeleton.model.User;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the User Repository
 */
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = Main.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {
    private String username;
    private String password;
    private String email;
    private User user = null;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public void setup() {
        username = UUID.randomUUID().toString().substring(0, 20);
        password = UUID.randomUUID().toString().substring(0, 20);
        email = UUID.randomUUID().toString().substring(0, 10) + "@" + UUID.randomUUID().toString().substring(0, 6) + ".de";
    }

    /**
     * Test create user
     */
    @Test
    @Order(1)
    public void testCreateUser() throws Exception {
        User toCreate = new User(username, password, email);
        toCreate.setRole(UserRole.ROLE_USER);

        assertDoesNotThrow(() -> {
            user = userRepository.save(toCreate);
        });

        assertNotNull(user);
    }

    @Test
    @Order(2)
    public void testBadCreateUser() throws Exception {
        User toCreate = new User(username, password, email);
        toCreate.setRole(UserRole.ROLE_USER);

        //Duplicate entries
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(toCreate));
    }

    /**
     * Test find by username.
     *
     * @throws Exception the exception
     */
    @Test
    @Order(2)
    public void testFindByUsername() throws Exception {
        Optional<User> foundUser = userRepository.findByUsername(username);
        assertEquals(user.getId(), foundUser.get().getId());
    }

    /**
     * Test find all users.
     *
     * @throws Exception the exception
     */
    @Test
    @Order(2)
    public void testFindAllUsers() throws Exception {
        List<User> allUsers = userRepository.findAll();
        List<Long> allUserIds = allUsers.stream().map(entry -> entry.getId()).collect(Collectors.toList());

        assertTrue(allUserIds.contains(user.getId()));
    }

    /**
     * Test exists by username.
     */
    @Test
    @Order(2)
    public void testExistsByUsername() {
        assertTrue(userRepository.existsByUsername(username));
    }

    /**
     * Test exists by email
     */
    @Test
    @Order(2)
    public void testExistsByEmail() {
        assertTrue(userRepository.existsByEmail(email));
        assertFalse(userRepository.existsByEmail("foobar"));
    }

    /**
     * Tests modifying user by adding new set of roles.
     */
    @Test
    @Order(3)
    void testModifyUser() {
        user.setRole(UserRole.ROLE_MANAGER);
        userRepository.save(user);

        assertEquals(UserRole.ROLE_MANAGER, user.getRole());
        assertNotEquals(UserRole.ROLE_USER, user.getRole());

        //Check if data is saved to db
        User modifiedUser = userRepository.findByUsername(username).get();

        assertEquals(UserRole.ROLE_MANAGER, modifiedUser.getRole());
    }

    /**
     * Tests deleting a user
     */
    @Test
    @Order(4)
    void testDeleteUser() {
        userRepository.delete(user);
        assertFalse(userRepository.existsByUsername(username));
    }
}
