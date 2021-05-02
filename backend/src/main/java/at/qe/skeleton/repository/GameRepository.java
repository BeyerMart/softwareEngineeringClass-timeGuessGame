package at.qe.skeleton.repository;

import at.qe.skeleton.model.Game;
import at.qe.skeleton.model.Topic;

import java.util.List;

public interface GameRepository extends AbstractRepository<Game, Long> {
        Game findById(long id);

        Boolean existsByName(String name);

        List<Game> findByTopic(Topic topic);
}
