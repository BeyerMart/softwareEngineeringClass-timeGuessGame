package at.qe.skeleton.repository;

import at.qe.skeleton.model.Topic;

/**
 *
 */
public interface TopicRepository extends AbstractRepository<Topic, Long> {
    Topic findById(long id);

    Boolean existsByName(String name);

}
