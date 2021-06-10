package at.qe.skeleton.services;

import at.qe.skeleton.controller.GameController;
import at.qe.skeleton.controller.UserController;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repository.GameRepository;
import at.qe.skeleton.repository.TeamRepository;
import at.qe.skeleton.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class StatisticService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    TeamService teamService;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    GameService gameService;
    @Autowired
    GameController gameController;
    @Autowired
    UserController userController;


    public int getNumberOfGames(Long userId){
        int totalNumberOfGames = 0;

        User user = userRepository.findById(userId).get();
        List<Team> allTeams = teamRepository.findAll();

        for(Team t : allTeams){
            if(t.getUsers().contains(user)){
                totalNumberOfGames++;
            }
        }

        return totalNumberOfGames;
    }

    public int getNumberOfWins(Long userId){
        int numberOfWins = 0;

        User user = userRepository.findById(userId).get();
        List<Game> allGames = gameRepository.findAll();

        for(Game g : allGames){
            if(g.getWinner() == null){
                continue;
            }
            if(g.getWinner().getUsers().contains(user)) {
                numberOfWins++;
            }
        }

        return numberOfWins;
    }

    public List<GameDto> getMatchHistory(Long userId){
        User user = userRepository.findById(userId).get();
        List<Team> allTeams = teamService.findAllTeams();
        List<Game> allGames = gameService.findAllGames();
        List<GameDto> participatedGames = new ArrayList<>();
        Game game = new Game();

        for(Team t : allTeams){
            if(t.getUsers().contains(user)){
                for(Game g : allGames){
                    if(g.getTeams().contains(t)){
                        participatedGames.add(gameController.convertToGameDto(g));
                    }
                }
            }
        }

        return participatedGames;
    }

    public List<UserDto> getLastPlayedWith(Long userId){
        User user = userRepository.findById(userId).get();
        List<GameDto> previousGames = getMatchHistory(userId);

/*        // If there are no previous games, just return own user (to bypass IndexOutOfBounds in previousGames.get(0))
        if(previousGames.isEmpty()){
            List<UserDto> defaultList = new ArrayList<>();
            defaultList.add(userController.convertToDto(user));

            return defaultList;
        }*/

        Collections.reverse(previousGames);
        List<Team> teams = new ArrayList<>(previousGames.get(0).getTeams());
        List<User> previousUsers = new ArrayList<>();
        List<UserDto> previousUsersDTO = new ArrayList<>();

        for(Team t : teams){
            if(t.getUsers().contains(user)){
                previousUsers.addAll(t.getUsers());
                for(User u : previousUsers){
                    previousUsersDTO.add(userController.convertToDto(u));
                }
                // Only information about last game played.
                // Change break condition if more games need to be covered
                break;
            }
        }

        return previousUsersDTO;
    }



}
