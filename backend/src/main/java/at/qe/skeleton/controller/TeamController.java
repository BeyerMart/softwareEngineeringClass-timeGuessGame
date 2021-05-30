package at.qe.skeleton.controller;

import at.qe.skeleton.exceptions.GameNotFoundException;
import at.qe.skeleton.exceptions.TeamNotFoundException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.payload.response.ErrorResponse;
import at.qe.skeleton.payload.response.SuccessResponse;
import at.qe.skeleton.payload.response.websocket.WSResponseType;
import at.qe.skeleton.payload.response.websocket.WebsocketResponse;
import at.qe.skeleton.repository.GameRepository;
import at.qe.skeleton.services.TeamService;
import at.qe.skeleton.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class TeamController {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private TeamService teamService;
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private VirtualUserController virtualUserController;

    @Autowired
    private UserService userService;

    @Autowired
    private UserController userController;

    @Autowired
    private SimpMessagingTemplate template;

    @PostMapping(value = "/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> createTeam(@Valid @RequestBody TeamDto newTeam) throws ParseException, TeamService.TeamExistsException {
        Optional<Game> game = gameRepository.findById(newTeam.getGame_id());
        if (!game.isPresent()) throw new GameNotFoundException(newTeam.getGame_id());

        Team team = convertToTeamEntity(newTeam);
        team.setGame(game.get());

        TeamDto createdTeam = convertToTeamDto(teamService.addTeam(team));

        template.convertAndSend("/game/" + game.get().getId(), (new WebsocketResponse(createdTeam, WSResponseType.TEAM_CREATED)).toString());

        return new ResponseEntity<>((new SuccessResponse(createdTeam, 201)).toString(), HttpStatus.CREATED);
    }

    @PatchMapping("teams/{id}")
    public ResponseEntity<?> updateTeam(@RequestBody Map<Object, Object> fields, @PathVariable Long id, UriComponentsBuilder uriComponentsBuilder) throws ParseException {
        Optional<Team> team = teamService.findTeam(id);
        if (!team.isPresent()) throw new TeamNotFoundException(id);
        Team existingTeam = team.get();

        Optional<User> currentUser = userService.getAuthenticatedUser();
        if (!currentUser.isPresent()) throw new org.springframework.security.access.AccessDeniedException("Unauthenticated");
        if (!existingTeam.getUsers().contains(currentUser.get()) && currentUser.get().getRole() != UserRole.ROLE_MANAGER && currentUser.get().getRole() != UserRole.ROLE_ADMIN)
            throw new org.springframework.security.access.AccessDeniedException("Not part of this team");

        if (fields.containsKey("name")) {
            existingTeam.setName((String) fields.get("name"));
        }
        if (fields.containsKey("points") && currentUser.get().getRole() == UserRole.ROLE_ADMIN) {
            existingTeam.setPoints(Long.valueOf(((Integer) fields.get("points")).longValue()));
        }

        TeamDto result = convertToTeamDto(teamService.updateTeam(existingTeam));

        //Send ws messages
        if(!team.get().getPoints().equals(result.getPoints())) {
            template.convertAndSend("/game/" + team.get().getGame().getId(), (new WebsocketResponse(result, WSResponseType.TEAM_POINTS_CHANGED)).toString());
        }
        if(!team.get().getName().equals(result.getName())) {
            template.convertAndSend("/game/" + team.get().getGame().getId(), (new WebsocketResponse(result, WSResponseType.TEAM_NAME_CHANGED)).toString());
        }


        return ResponseEntity.ok((new SuccessResponse(result)).toString());
    }


    @GetMapping("/teams/{id}")
    private ResponseEntity<?> searchTeam(@PathVariable Long id) {
        Optional<Team> team = teamService.findTeam(id);
        if (!team.isPresent()) throw new TeamNotFoundException(id);

        TeamDto result = convertToTeamDto(team.get());

        return ResponseEntity.ok((new SuccessResponse(result)).toString());
    }

    @GetMapping("/teams/{id}/users")
    private ResponseEntity<?> getAllTeamUsers(@PathVariable Long id) {
        Optional<Team> team = teamService.findTeam(id);
        if (!team.isPresent()) throw new TeamNotFoundException(id);

        List<Object> users = teamService.getAllTeamUsers(team.get()).stream().map(user -> {
            if (user.getClass().getName().equals("at.qe.skeleton.model.User")) {
                return userController.convertToDto((User)user);
            } else {
                return virtualUserController.convertToDto((VirtualUser) user);
            }
        }).collect(Collectors.toList());

        return ResponseEntity.ok((new SuccessResponse(users)).toString());
    }

    @PostMapping("/teams/{id}/users")
    private ResponseEntity<?> addUserToTeam(@Valid @RequestBody(required = false) VirtualUserDto user, @PathVariable Long id) throws ParseException {
        Optional<Team> team = teamService.findTeam(id);
        if (!team.isPresent()) throw new TeamNotFoundException(id);
        Team existingTeam = team.get();

        Optional<User> currentUser = userService.getAuthenticatedUser();
        if (!currentUser.isPresent()) throw new org.springframework.security.access.AccessDeniedException("Unauthenticated");

        Object userDto = null;

        WSResponseType type;

        if (user != null) {
            //Virtual user
            if (!team.get().getUsers().contains(currentUser.get()) && currentUser.get().getRole() != UserRole.ROLE_MANAGER && currentUser.get().getRole() != UserRole.ROLE_ADMIN)
                throw new org.springframework.security.access.AccessDeniedException("Not part of this team");

            user.setCreator_id(currentUser.get().getId());

            VirtualUser result = teamService.addVirtualUser(team.get(), virtualUserController.convertToEntity(user));

            if(result == null) return new ResponseEntity<>(((new ErrorResponse("Virtual user already exists", 400)).toString()), HttpStatus.BAD_REQUEST);

            userDto = modelMapper.map(result, VirtualUserDto.class);

            type = WSResponseType.VIRTUAL_USER_JOINED;
        } else {
            //Own user
            teamService.addUser(existingTeam, currentUser.get());

            userDto = modelMapper.map(currentUser.get(), UserDto.class);

            type = WSResponseType.USER_JOINED_TEAM;
        }

        TeamDto teamDto = convertToTeamDto(existingTeam);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("user", mapper.valueToTree(userDto));
        rootNode.set("team", mapper.valueToTree(teamDto));

        template.convertAndSend("/game/" + team.get().getGame().getId(), (new WebsocketResponse(rootNode, type)).toString());

        return new ResponseEntity<>((new SuccessResponse(userDto, 201)).toString(), HttpStatus.CREATED);
    }

    @DeleteMapping("/teams/{id}/users")
    private ResponseEntity<?> removeUserFromTeam(@PathVariable Long id) {
        Optional<Team> team = teamService.findTeam(id);
        if (!team.isPresent()) throw new TeamNotFoundException(id);
        Team existingTeam = team.get();

        Optional<User> user = userService.getAuthenticatedUser();
        if (!user.isPresent()) throw new org.springframework.security.access.AccessDeniedException("Unauthenticated");

        Team result = teamService.removeUser(existingTeam, user.get());
        TeamDto convertedTeam = convertToTeamDto(team.get());
        if(result == null) {
            //Team is deleted
            template.convertAndSend("/game/" + team.get().getGame().getId(), (new WebsocketResponse(convertedTeam, WSResponseType.TEAM_DELETED)).toString());
        } else {
            UserDto userDto = userController.convertToDto(user.get());

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();
            rootNode.set("user", mapper.valueToTree(userDto));
            rootNode.set("team", mapper.valueToTree(convertedTeam));

            template.convertAndSend("/game/" + team.get().getGame().getId(), (new WebsocketResponse(rootNode, WSResponseType.USER_LEFT_TEAM)).toString());
        }

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/teams/{id}/users/{virtualId}")
    private ResponseEntity<?> removeUserFromTeam(@PathVariable Long id, @PathVariable Long virtualId) {
        Optional<Team> team = teamService.findTeam(id);
        if (!team.isPresent()) throw new TeamNotFoundException(id);

        VirtualUser result = teamService.removeVirtualUser(team.get(), virtualId);

        if(result != null) {
            TeamDto teamDto = convertToTeamDto(team.get());
            VirtualUserDto userDto = virtualUserController.convertToDto(result);

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();
            rootNode.set("user", mapper.valueToTree(userDto));
            rootNode.set("team", mapper.valueToTree(teamDto));

            template.convertAndSend("/game/" + team.get().getGame().getId(), (new WebsocketResponse(rootNode, WSResponseType.VIRTUAL_USER_LEFT)).toString());
        }

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllTeams() {
        List<TeamDto> teams = teamService.findAllTeams().stream().map(this::convertToTeamDto).collect(Collectors.toList());

        return ResponseEntity.ok((new SuccessResponse(teams)).toString());
    }


    @DeleteMapping("/teams/{id}")
    private ResponseEntity<?> deleteTeam(@PathVariable Long id) {
        Optional<Team> team = teamService.findTeam(id);
        if (!team.isPresent()) throw new TeamNotFoundException(id);

        teamService.deleteTeam(team.get());
        TeamDto convertedTeam = convertToTeamDto(team.get());
        template.convertAndSend("/game/" + team.get().getGame().getId(), (new WebsocketResponse(convertedTeam, WSResponseType.TEAM_DELETED)).toString());

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    /**
     * Convert methods -> DTOs TO ENTITY and vice versa
     */
    public TeamDto convertToTeamDto(Team team) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(Team.class, TeamDto.class).addMappings(m -> m.map(src -> src.getGame().getId(), TeamDto::setGame_id));
        return modelMapper.map(team, TeamDto.class);
    }

    public Team convertToTeamEntity(TeamDto teamDto) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(teamDto, Team.class);
    }
}
