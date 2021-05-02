package at.qe.skeleton.controller;

import at.qe.skeleton.model.*;
import at.qe.skeleton.payload.response.SuccessResponse;
import at.qe.skeleton.payload.response.websocket.WSResponseType;
import at.qe.skeleton.payload.response.websocket.WebsocketResponse;
import at.qe.skeleton.repository.GameRepository;
import at.qe.skeleton.repository.TeamRepository;
import at.qe.skeleton.services.GameService;
import at.qe.skeleton.services.TeamService;
import at.qe.skeleton.services.TopicService;
import at.qe.skeleton.services.UserService;
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

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private ResponseEntity<?> createGame(@Valid @RequestBody GameDto newGame) throws ParseException, GameService.GameExistsException{
        GameDto game = convertToGameDto(gameService.addGame(convertToGameEntity(newGame), newGame.getTopicId()));
        gameCreatedResponse(game);
        return new ResponseEntity<>((new SuccessResponse(game, 201)).toString(),HttpStatus.CREATED);
    }


    @GetMapping(value = "/games", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllGames() {
        List<GameDto> games = gameService.findAllGames().stream().map(this::convertToGameDto).collect(Collectors.toList());

        return ResponseEntity.ok((new SuccessResponse(games)).toString());
    }


    @DeleteMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private ResponseEntity<?> deleteGame(@PathVariable Long gameId){
        GameDto game = convertToGameDto(gameService.findGame(gameId).get());
        gameService.deleteGame(gameService.findGame(gameId).get());
        gameDeletedResponse(game);

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }


    @PatchMapping("/games/{gameId}")
    private ResponseEntity<?> updateGameTopic(@RequestBody Map<String, Long> fields, @PathVariable Long gameId, UriComponentsBuilder uriComponentsBuilder) {
        GameDto currentGame = convertToGameDto(gameService.findGame(gameId).get());
        Long newTopicId = fields.get("topicId");
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


    /**
     * Convert methods -> DTOs TO ENTITY and vice versa
     */
    private GameDto convertToGameDto(Game game) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(Game.class, GameDto.class).addMappings(m -> m.map(src -> src.getTopic().getId(), GameDto::setTopicId));
        return modelMapper.map(game, GameDto.class);
    }

    private Game convertToGameEntity(GameDto gameDto) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(GameDto.class, Game.class).addMappings(m -> m.map(GameDto::getTopicId, Game::setTopic));
        return modelMapper.map(gameDto, Game.class);
    }
}
