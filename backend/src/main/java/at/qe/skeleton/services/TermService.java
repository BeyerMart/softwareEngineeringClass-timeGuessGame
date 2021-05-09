package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.TermNotFoundException;
import at.qe.skeleton.model.Term;
import at.qe.skeleton.model.Topic;
import at.qe.skeleton.repository.TermRepository;
import at.qe.skeleton.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TermService {
    private static final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TermRepository termRepository;

    /**
     * DISCLAIMER: HAS TO HAVE A GIVEN TOPIC ALREADY. FIRST CREATE TOPIC, THEN TERM!
     * @param id TOPIC id
     * @param term term to be added
     * @return new term
     */
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public Term addTerm (Long id, Term term){
        Topic topic = topicRepository.getOne(id);

        term.setCreated_at(timestamp);
        term.setUpdated_at(timestamp);
        term.setTopic(topic);
        term.setCorrect_guesses(0L);
        term.setAppearances(0L);

        topic.getTerms().add(term);
        return termRepository.save(term);
    }

    /**
     * @throws TermNotFoundException
     * @param id id of searched term
     * @return searched term
     */
    public Term findTerm(Long id){
        if(!termRepository.existsById(id)){
            throw (new TermNotFoundException(id));
        }
        return termRepository.getOne(id);
    }

    /**
     * Find all existing terms
     * @return all terms
     */
    public List<Term> findAllTerms(Topic topic) {
        return termRepository.findByTopic(topic);
    }

    /**
     * @throws TermNotFoundException
     * @param term term to be deleted
     */
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public void deleteTerm(Term term){
        if(!termRepository.existsById(term.getId())){
            throw (new TermNotFoundException(term.getId()));
        }
        termRepository.delete(term);
    }

}
