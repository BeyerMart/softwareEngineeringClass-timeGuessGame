package at.qe.skeleton.controller;

import at.qe.skeleton.exceptions.TeamNotFoundException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.payload.response.websocket.WSResponseType;
import at.qe.skeleton.payload.response.websocket.WebsocketResponse;
import at.qe.skeleton.repository.TeamRepository;
import at.qe.skeleton.services.CubeService;
import at.qe.skeleton.services.GameService;
import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.TeamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController()
public class GameplayController {

    private final int PRE_ROUND_TIME = 10;
    private final long VOTE_TIME = 30;

    @Autowired
    SimpMessagingTemplate template;
    @Autowired
    GameService gameService;
    @Autowired
    GameController gameController;
    @Autowired
    CubeService cubeService;
    @Autowired
    CubeController cubeController;
    @Autowired
    RoomService roomService;
    @Autowired
    RoomController roomController;
    @Autowired
    TeamController teamController;
    @Autowired
    UserController userController;
    @Autowired
    TeamService teamService;
    @Autowired
    VirtualUserController virtualUserController;
    @Autowired
    TeamRepository teamRepository;

    public void rollTheDiceMessage(Game game) {
        template.convertAndSend("/game/" + game.getId(), (new WebsocketResponse(gameController.convertToGameDto(game), WSResponseType.ROLL_DICE)).toString());
    }

    public void roundStart(Game game, int round) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("round", round);

        template.convertAndSend("/game/" + game.getId(), (new WebsocketResponse(node, WSResponseType.GAMEPLAY_ROUND_START)).toString());
    }

    public void preRoundTimerStartMessage(Game game) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("pre_round_time", PRE_ROUND_TIME);

        template.convertAndSend("/game/" + game.getId(), (new WebsocketResponse(node, WSResponseType.GAMEPLAY_PRE_ROUND_TIMER)).toString());
    }

    public void timerStartMessage(Game game, int time) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("time", time * 60);

        template.convertAndSend("/game/" + game.getId(), (new WebsocketResponse(node, WSResponseType.GAMEPLAY_TIMER)).toString());
    }

    public void gameOverMessage(Game game, Team winner) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.set("winner", mapper.valueToTree(teamController.convertToTeamDto(winner)));

        template.convertAndSend("/game/" + game.getId(), (new WebsocketResponse(node, WSResponseType.GAME_OVER)).toString());
    }

    public void gameDeletedMessage(Game game) {
        template.convertAndSend("/game/" + game.getId(), (new WebsocketResponse(gameController.convertToGameDto(game), WSResponseType.GAME_DELETED)).toString());
    }

    public void roomDeletedMessage(Game game, Room room) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.set("room", mapper.valueToTree(room));

        template.convertAndSend("/game/" + game.getId(), (new WebsocketResponse(node, WSResponseType.ROOM_DELETED)).toString());
    }

    public void cubeInformationMessage(Game game, Activity activity, int potentialPoints, int time, Term term) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("points", potentialPoints);
        node.put("time", time * 60);
        node.put("term", term.getName());
        node.put("activity", activity.toString());

        template.convertAndSend("/game/" + game.getId(), (new WebsocketResponse(node, WSResponseType.GAMEPLAY_CUBE_INFORMATION)).toString());
    }

    public void currentTeamAndUserMessage(Game game, Team team, Object user) {
        TeamDto teamDto = teamController.convertToTeamDto(team);

        Object userDto;
        if (user.getClass().getName().equals("at.qe.skeleton.model.User")) {
            userDto = userController.convertToDto((User) user);
        } else {
            userDto = virtualUserController.convertToDto((VirtualUser) user);
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.set("team", mapper.valueToTree(teamDto));
        node.set("user", mapper.valueToTree(userDto));

        template.convertAndSend("/game/" + game.getId(), (new WebsocketResponse(node, WSResponseType.GAMEPLAY_CURRENT_TEAMUSER)).toString());
    }

    public void sleepUntilRoomNotEmpty(Game game) throws InterruptedException {
        boolean otherPlayerJoined = false;
        while(!otherPlayerJoined) {
            otherPlayerJoined = game.getTeams().stream().mapToInt(team -> {
                Team teamDb = teamService.findTeam(team.getId()).orElseThrow(() -> new TeamNotFoundException(team.getId()));
                return teamDb.getUsers() != null && teamDb.getUsers().size() > 1 ? 1 : 0;
            }).sum() >= 2;
            TimeUnit.SECONDS.sleep(10);
        }
    }

    public void pointValidationStop(Game game) {
        template.convertAndSend("/game/" + game.getId(), (new WebsocketResponse(gameController.convertToGameDto(game), WSResponseType.POINT_VALIDATION_STOP)).toString());
    }

    public void pointValidationStart(Game game) {
        template.convertAndSend("/game/" + game.getId(), (new WebsocketResponse(gameController.convertToGameDto(game), WSResponseType.POINT_VALIDATION_START)).toString());
    }

    public void gameplay(Game game) throws InterruptedException {
        //Cube information
        int rolledFacet;
        int potentialPoints;
        Activity activity;
        int availableTime;
        int round = 1;

        Optional<Room> optionalRoom = roomService.getRoomById(game.getRoom_id());
        Room room = optionalRoom.get();

        List<Team> teams = new ArrayList<>();
        HashMap<Long, List<Object>> userHashMap = new HashMap<>();

        //Randomize terms
        List<Term> terms = new ArrayList<>(game.getTopic().getTerms());
        Collections.shuffle(terms);

        //Wait until at least one more player (in a different team) is connected
        sleepUntilRoomNotEmpty(game);
        TimeUnit.SECONDS.sleep(5); //Wait another 5 seconds

        //Activates cube notifications
        cubeService.startFacetNotification(game.getRoom_id().intValue());

        while (true) {
            /*
             * End game if game does not exist anymore
             */
            Optional<Game> gameOptional = gameService.findGame(game.getId());
            if (!gameOptional.isPresent()) {
                gameDeletedMessage(game);
                return;
            }
            game = gameOptional.get();

            /*
             * End game if room does not exist anymore
             */
            if (!roomService.getRoomById(room.getId()).isPresent()) {
                roomDeletedMessage(game, room);
                break;
            }

            /*
             * End game if everyone left
             */
            List<User> allUsersInGame = game.getTeams().stream().map(Team::getUsers).collect(ArrayList::new, List::addAll, List::addAll);
            if (allUsersInGame.isEmpty()) break;


            /*
             * End game if there are no terms left
             */
            if (terms.isEmpty()) break;

            roundStart(game, round);

            /*
             * Once every team had its turn, the list of teams is getting reset
             */
            if (teams.size() == 0) {
                teams = new ArrayList<>(game.getTeams());
                Collections.shuffle(teams);
            }

            /*
             * Get random team and user
             */
            Team currentTeam = teams.remove(0);

            List<Object> availableUsers = userHashMap.getOrDefault(currentTeam.getId(), new ArrayList<>());
            if (availableUsers.size() == 0) {
                //everybody has already been selected, refill
                availableUsers = teamService.getAllTeamUsers(currentTeam);
            }

            Object currentUser = availableUsers.remove(0);
            userHashMap.put(currentTeam.getId(), availableUsers);

            currentTeamAndUserMessage(game, currentTeam, currentUser);

            /*
             * Dice has to be rolled message
             */
            rollTheDiceMessage(game);

            //TODO
            // maybe w/ button "I ROLLED THE DICE" then continue

            /*
             * Get information from cube (points, type and time)
             * Get random term
             */
            int rolledOldFacet = room.getCube().getFacet();
            rolledFacet = rolledOldFacet;

            //"wait" until new facet is present
            while (rolledOldFacet == rolledFacet) {
                rolledFacet = room.getCube().getFacet();
            }

            potentialPoints = cubeService.getPointsFromFacet(rolledFacet);
            activity = cubeService.getActivityFromFacet(rolledFacet);
            availableTime = cubeService.getTimeFromFacet(rolledFacet);
            Term currentTerm = terms.remove(0);

            /*
             * Send round-relevant information
             */
            cubeInformationMessage(game, activity, potentialPoints, availableTime, currentTerm);

            /*
             * Player has time to prepare for 10 seconds
             */
            preRoundTimerStartMessage(game);
            TimeUnit.SECONDS.sleep(PRE_ROUND_TIME);

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            Timestamp endTime = new Timestamp(System.currentTimeMillis() + (long)availableTime * 60000);

            /*
             * Timer is now starting
             */
            timerStartMessage(game, availableTime);

            //Either time is over or word is guessed (i.e. cube is turned)
            rolledFacet = room.getCube().getFacet();
            while (endTime.after(currentTime) && rolledFacet == room.getCube().getFacet()) {
                currentTime = new Timestamp(System.currentTimeMillis());
            }

            /*
             * Time to vote whether the round was fair or not
             */
            gameService.addGamePoints(game, currentTeam);
            pointValidationStart(game);
            currentTime = new Timestamp(System.currentTimeMillis());

            endTime = new Timestamp(System.currentTimeMillis() + (VOTE_TIME * 1000));
            GamePoints gamePoints = gameService.getGamePoints(game);

            while(endTime.after((currentTime)) && ((gamePoints.getRejections() + gamePoints.getConfirmations()) != (allUsersInGame.size() - currentTeam.getUsers().size()))){
                currentTime = new Timestamp(System.currentTimeMillis());
                gamePoints = gameService.getGamePoints(game);
            }

            pointValidationStop(game);
            gameService.removeGamePoints(game);

            /*
             * Add points if voting claims that the round was fair
             */
            if (gamePoints.getConfirmations() > gamePoints.getRejections()) {
                currentTeam.setPoints(currentTeam.getPoints() + potentialPoints);
            } else {
                //-1 point for cheating
                currentTeam.setPoints(currentTeam.getPoints() - 1);
            }
            teamRepository.save(currentTeam);

            /*
             * Primary win condition
             */
            if (currentTeam.getPoints() >= game.getMax_points()) break;

            round++;
        }

        //cube stop facet notification
        cubeService.stopFacetNotification(game.getRoom_id().intValue());

        //Find winner (i.e. the team with most points)
        Team winner = null;
        for (Team team : game.getTeams()) {
            if (winner == null || winner.getPoints() < team.getPoints()) {
                winner = team;
            }
        }

        game.setWinner(winner);
        gameService.updateGame(game);

        /*
         * Game over socket message
         */
        gameOverMessage(game, winner);
    }
}
