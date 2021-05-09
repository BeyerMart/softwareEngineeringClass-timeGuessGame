package at.qe.skeleton.services;
import at.qe.skeleton.model.User;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service for accessing and manipulating user data.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Service
public class UserService {
    public class UsernameExistsException extends Exception {}
    public class EmailExistsException extends Exception {}

    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LogManager.getLogger(UserService.class);

     /**
     * Checks if user exists
     *
     */
    public boolean userExitsById(Long id) {
        return userRepository.existsById(id);
    }

    /**
     * Returns a collection of all users.
     *
     * @return
     */

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Loads a single user identified by its id.
     *
     * @param id the username to search for
     * @return the user with the given id
     */

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     *  Creates a new user saves it in the user repository. The roles are checked because only an admin can create admin users.
     *
     * @throws AccessDeniedException
     * @throws UsernameExistsException
     * @throws EmailExistsException
     * @param user the user to create
     * @return the newly created user
     */
    public User createNewUser(User user) throws AccessDeniedException, UsernameExistsException, EmailExistsException {
        if (userRepository.existsByUsername(user.getUsername())) {
			throw( new UsernameExistsException());
		}
		if (userRepository.existsByEmail(user.getEmail())) {
			throw( new EmailExistsException());
		}
        checkForHigherRoles(user);
        return userRepository.save(user);
    }

    /**
     * Checks a user object's roles and compares it with the current authentication.
     * If the roles in the user object are higher than the granted authorities a AccessDeniedException is thrown.
     *
     * @param user the user the contains the roles to be checked
     * @throws AccessDeniedException
     */
    public void checkForHigherRoles(User user) throws AccessDeniedException {
        UserRole role = user.getRole();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(role == UserRole.ROLE_ADMIN) {
            if (auth == null || auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                throw (new AccessDeniedException("Insufficient authority"));
            }
        } else if (role == UserRole.ROLE_MANAGER) {
            if(auth == null || auth.getAuthorities().stream()
                    .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"))) {
                throw (new AccessDeniedException("Insufficient authority"));
            }
        }
    }

    /**
     * Deletes the user.
     *
     * @param user the user to delete
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteUser(User user) {
        checkForHigherRoles(user);
        userRepository.delete(user);
        log.info(getAuthenticatedUser().get().getUsername() + " deleted user " + user.getUsername());
    }

    /**
     * Updates a user.
     * Checks correct permissions before updating User.
     * @param user the user to update
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public User updateUser(User user) {
        User authenticatedUser = getAuthenticatedUser().get();
        if (authenticatedUser.getId().equals(user.getId()) && authenticatedUser.getRole() == user.getRole())
            return userRepository.save(user);
        if (authenticatedUser.getRole() == UserRole.ROLE_USER)
            throw new AccessDeniedException("Insufficient Authority");
        User oldUser = userRepository.findById(user.getId()).get();
        if (authenticatedUser.getRole() == UserRole.ROLE_MANAGER && (oldUser.getRole() != UserRole.ROLE_USER || oldUser.getRole() != user.getRole()))
            throw new AccessDeniedException("Insufficient Authority");
        return userRepository.save(user);
    }

    /**
     * Gets current authenticated user
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Optional<User> getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName());
    }
}
