package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.GameNotFoundException;
import at.qe.skeleton.exceptions.TopicNotFoundException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repository.GameRepository;
import at.qe.skeleton.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class GameService {

    public class GameExistsException extends Exception {}
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private GameRepository gameRepository;

    /**
     *
     * @param game to be added / created
     * @param id topicId
     * @return created game
     * @throws GameExistsException if game already exists
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Game addGame (Game game, Long id) throws GameService.GameExistsException {
        if(gameRepository.existsByName(game.getName())){
            throw (new GameService.GameExistsException());
        }
        Game newGame = new Game();
        Topic topic;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        topic = topicRepository.getOne(id);
        newGame.setName(game.getName());
        newGame.setCreated_at(timestamp);
        newGame.setTopic(topic);

        return gameRepository.save(newGame);
    }

    /**
     *
     * @param game to be updated
     * @return updated game
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Game updateGame(Game game) {
        return gameRepository.save(game);
    }

    /**
     *
     * @param id Id of the searched game
     * @return game
     * @throws GameNotFoundException if game was not found
     */
    public Optional<Game> findGame(Long id) throws GameNotFoundException {
        if(!gameRepository.existsById(id)){
            throw (new GameNotFoundException(id));
        }
        return gameRepository.findById(id);
    }

    public List<Game> findAllGames() {
        return gameRepository.findAll();
    }

    public List<Game> findAllGamesByTopic(Topic topic) {

        return gameRepository.findByTopic(topic);
    }

    /**
     *
     * @param game to be deleted
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteGame(Game game){
        if(!gameRepository.existsById(game.getId())){
            throw (new GameNotFoundException(game.getId()));
        }

        gameRepository.delete(game);
    }

}
