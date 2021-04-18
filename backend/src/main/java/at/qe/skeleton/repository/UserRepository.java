package at.qe.skeleton.repository;

import at.qe.skeleton.model.User;

import java.util.Optional;

/**
 *
 */
public interface UserRepository extends AbstractRepository<User, Long> {
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
}
