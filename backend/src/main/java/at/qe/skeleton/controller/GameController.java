package at.qe.skeleton.controller;

import at.qe.skeleton.exceptions.GameNotFoundException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.payload.response.ErrorResponse;
import at.qe.skeleton.payload.response.SuccessResponse;
import at.qe.skeleton.payload.response.websocket.WSResponseType;
import at.qe.skeleton.payload.response.websocket.WebsocketResponse;
import at.qe.skeleton.repository.GameRepository;
import at.qe.skeleton.repository.TeamRepository;
import at.qe.skeleton.services.*;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.security.access.AccessDeniedException;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class GameController {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private TopicService topicService;
    @Autowired
    private GameService gameService;
    @Autowired
    SimpMessagingTemplate template;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    TeamController teamController;
    @Autowired
    UserService userService;
    @Autowired
    GameplayController gameplayController;
    @Autowired
    RoomService roomService;
    @Autowired
    TeamService teamService;

    public void gameCreatedResponse(GameDto game){
        template.convertAndSend("/game/" + game.getId(), (new WebsocketResponse(game, WSResponseType.GAME_CREATED)).toString());
    }

    public void gameTopicUpdatedResponse(GameDto game){
        template.convertAndSend("/game/" + game.getId(), (new WebsocketResponse(game, WSResponseType.GAME_TOPIC_CHANGED)).toString());
    }

    public void gameDeletedResponse(GameDto game){
        template.convertAndSend("/game/" + game.getId(), (new WebsocketResponse(game, WSResponseType.GAME_DELETED)).toString());
    }

    @PostMapping(value = "/games", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> createGame(@RequestBody Map<String, Long> fields) throws ParseException, TeamService.TeamExistsException {
        if(!fields.containsKey("room_id")) return new ResponseEntity<>(((new ErrorResponse("'room_id' is not present", 400)).toString()), HttpStatus.BAD_REQUEST);

        Optional<Room> optionalRoom = roomService.getRoomById(fields.get("room_id"));
        if(!optionalRoom.isPresent()) return new ResponseEntity<>(((new ErrorResponse("Room with 'room_id' doesn't exist", 400)).toString()), HttpStatus.BAD_REQUEST);
        Room room = optionalRoom.get();
        if(room.getTopic_id() == null) return new ResponseEntity<>(((new ErrorResponse("Room doesn't have a 'topic_id'", 400)).toString()), HttpStatus.BAD_REQUEST);

        //Copy values from room
        Game game = new Game();
        game.setName(room.getRoom_name());
        game.setMax_points(room.getMax_points());
        game.setRoom_id(room.getRoom_id());

        game = gameService.addGame(game, room.getTopic_id());

        //Set teams
        for(VirtualTeam virtTeam : room.getTeams().values()) {
            Team team = new Team();
            team.setGame(game);
            team.setName(virtTeam.getTeam_name());
            team = teamService.addTeam(team);

            //Add (virtual) users
            for(UserIdVirtualUser user : virtTeam.getPlayers()) {
                //Only add creator (others are added later)
                if(user.getUser_id() == room.getHost_id()) {
                    Optional<User> optionalUser = userService.getUserById(user.getUser_id());
                    if (optionalUser.isPresent()) {
                        Team finalTeam = team;
                        user.getVirtualUsers().values().forEach(virtUser -> teamService.addVirtualUser(finalTeam, virtUser));
                        team = teamService.addUser(team, optionalUser.get());
                    }
                }
            }
        }

        //Start gameplay in new thread
        Game finalGame = game;
        new Thread(() -> {
            try {
                gameplayController.gameplay(finalGame);
            } catch (InterruptedException e) {
                e.printStackTrace(); //Sleep timers may be interrupted
            }
        });

        GameDto gameDto = convertToGameDto(game);
        gameCreatedResponse(gameDto);
        return new ResponseEntity<>((new SuccessResponse(gameDto, 201)).toString(),HttpStatus.CREATED);
    }


    @GetMapping(value = "/games", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllGames() {
        List<GameDto> games = gameService.findAllGames().stream().map(this::convertToGameDto).collect(Collectors.toList());

        return ResponseEntity.ok((new SuccessResponse(games)).toString());
    }


    @DeleteMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private ResponseEntity<?> deleteGame(@PathVariable Long gameId){
        Game game = gameService.findGame(gameId).get();
        GameDto gameDto = convertToGameDto(game);

        //Unset winning team (due to db reference)
        if(game.getWinner() != null) {
            game.setWinner(null);
            game = gameService.updateGame(game);
        }

        //Delete teams
        for(Team team : game.getTeams()) {
            teamService.deleteTeam(team);
        }

        gameService.deleteGame(game);
        gameDeletedResponse(gameDto);

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }


    @PatchMapping("/games/{gameId}")
    private ResponseEntity<?> updateGameTopic(@RequestBody Map<String, Long> fields, @PathVariable Long gameId, UriComponentsBuilder uriComponentsBuilder) {
        GameDto currentGame = convertToGameDto(gameService.findGame(gameId).get());
        Long newTopicId = fields.get("topic_id");
        if(newTopicId == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Topic newTopic = topicService.findTopic(newTopicId);

        Game updatedGame = convertToGameEntity(currentGame);
        updatedGame.setTopic(newTopic);
        GameDto game = convertToGameDto(gameService.updateGame(updatedGame));
        gameTopicUpdatedResponse(game);

        return ResponseEntity.ok((new SuccessResponse(game)).toString());
    }

    @GetMapping("/games/{id}")
    private ResponseEntity<?> searchGame(@PathVariable Long id){
        GameDto game = convertToGameDto(gameService.findGame(id).get());

        return ResponseEntity.ok((new SuccessResponse(game)).toString());
    }

    @GetMapping(value = "/topics/{topicId}/games", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllGamesByTopic(@PathVariable Long topicId) {
        Topic topic = topicService.findTopic(topicId);
        List<GameDto> games = gameService.findAllGamesByTopic(topic).stream().map(this::convertToGameDto).collect(Collectors.toList());
        return ResponseEntity.ok((new SuccessResponse(games)).toString());
    }

    @GetMapping(value = "/games/{gameId}/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllTeams(@PathVariable Long gameId) {
        Game game = gameService.findGame(gameId).get();

        List<TeamDto> teams = game.getTeams().stream().map(x -> teamController.convertToTeamDto(x)).collect(Collectors.toList());
        return ResponseEntity.ok((new SuccessResponse(teams)).toString());
    }

    @PostMapping(value = "/games/{gameId}/points", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> confirmPoints(@PathVariable Long gameId) throws GameNotFoundException {
        Optional<Game> game = gameService.findGame(gameId);
        if(!game.isPresent()) throw new GameNotFoundException(gameId);

        String error = gameService.confirmPoints(game.get());
        if(error != null) return new ResponseEntity<>(new ErrorResponse(error, 403).toString(), HttpStatus.FORBIDDEN);

        return ResponseEntity.ok((new SuccessResponse(null)).toString());
    }

    @PutMapping(value = "/games/{gameId}/points", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> rejectPoints(@PathVariable Long gameId) throws GameNotFoundException {
        //https://stackoverflow.com/questions/47817716/restful-delete-with-reason
        //According to RFC, PUT may yield a soft delete. In this case, a point reduction corresponds to a soft delete (at least in some way)
        Optional<Game> game = gameService.findGame(gameId);
        if(!game.isPresent()) throw new GameNotFoundException(gameId);

        String error = gameService.rejectPoints(game.get());
        if(error != null) return new ResponseEntity<>(new ErrorResponse(error, 403).toString(), HttpStatus.FORBIDDEN);

        return ResponseEntity.ok((new SuccessResponse(null)).toString());
    }

    /**
     * Convert methods -> DTOs TO ENTITY and vice versa
     */
    public GameDto convertToGameDto(Game game) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(Game.class, GameDto.class).addMappings(m -> m.map(src -> src.getTopic().getId(), GameDto::setTopic_id));
        modelMapper.typeMap(Game.class, GameDto.class).addMappings(m -> m.map(src -> src.getWinner().getId(), GameDto::setWinner_id));
        return modelMapper.map(game, GameDto.class);
    }

    public Game convertToGameEntity(GameDto gameDto) {
        //modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        //modelMapper.typeMap(GameDto.class, Game.class).addMappings(m -> m.map(GameDto::getTopicId, Game::setTopic));
        //return modelMapper.map(gameDto, Game.class);
        return modelMapper.map(gameDto, Game.class);
    }


}
