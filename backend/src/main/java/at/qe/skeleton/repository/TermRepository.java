package at.qe.skeleton.repository;

import at.qe.skeleton.model.Term;
import at.qe.skeleton.model.Topic;

import java.util.List;

public interface TermRepository extends AbstractRepository<Term, Long> {
    /**
     * Used to get all terms by Topic
     * @param topic
     * @return
     */
    List<Term> findByTopic(Topic topic);
}