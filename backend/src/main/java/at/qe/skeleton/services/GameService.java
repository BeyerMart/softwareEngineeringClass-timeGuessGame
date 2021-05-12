package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.GameNotFoundException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repository.GameRepository;
import at.qe.skeleton.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class GameService {
    private final ConcurrentHashMap<Long, GamePoints> gamePoints = new ConcurrentHashMap<>();

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserService userService;

    /**
     *
     * @param game to be added / created
     * @param id topicId
     * @return created game
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Game addGame (Game game, Long id) {
        Topic topic = topicRepository.getOne(id);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        game.setCreated_at(timestamp);
        game.setTopic(topic);
        game.setTeams(new HashSet<>());

        return gameRepository.save(game);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String confirmPoints(Game game) {
        Optional<User> currentUser = userService.getAuthenticatedUser();

        String error = checkGamePoints(game, currentUser.get());
        if(error != null) return error;

        synchronized (gamePoints) {
            GamePoints pointsObj = gamePoints.get(game.getId());
            pointsObj.incrementConfirmations();
            pointsObj.addUserId(currentUser.get().getId());
        }
        return null;
    }

    public void addGamePoints(Game game, Team currentTeam) {
        gamePoints.put(game.getId(), new GamePoints(currentTeam));
    }

    public GamePoints getGamePoints(Game game) {
        return gamePoints.getOrDefault(game.getId(), null);
    }

    public GamePoints removeGamePoints(Game game) {
        return gamePoints.remove(game.getId());
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String rejectPoints(Game game) {
        Optional<User> currentUser = userService.getAuthenticatedUser();

        String error = checkGamePoints(game, currentUser.get());
        if(error != null) return error;

        synchronized (gamePoints) {
            GamePoints pointsObj = gamePoints.get(game.getId());
            pointsObj.incrementRejections();
            pointsObj.addUserId(currentUser.get().getId());
        }
        return null;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String checkGamePoints(Game game, User user) {
        //Check if user is a player
        List<User> users = game.getTeams().stream().map(Team::getUsers).collect(ArrayList::new, List::addAll, List::addAll);
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        if(!userIds.contains(user.getId())) return "You are not a player";

        synchronized (gamePoints) {
            //Check if game is running
            if(!gamePoints.containsKey(game.getId())) return "The game is not running";

            GamePoints pointsObj = gamePoints.get(game.getId());

            //Check if already voted
            if(pointsObj.getUserIds().contains(user.getId())) return "You have already voted";

            //Check if member of current team
            List<Long> userIdsCurrentTeam = pointsObj.getCurrentTeam().getUsers().stream().map(User::getId).collect(Collectors.toList());
            if(userIdsCurrentTeam.contains(user.getId())) return "You are in the current active team";
        }
        return null;
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
