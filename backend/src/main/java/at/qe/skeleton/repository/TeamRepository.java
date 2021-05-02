package at.qe.skeleton.repository;

import at.qe.skeleton.model.Team;

/**
 *
 */
public interface TeamRepository extends AbstractRepository<Team, Long> {
    Team findById(long id);

    Boolean existsByName(String name);
}
