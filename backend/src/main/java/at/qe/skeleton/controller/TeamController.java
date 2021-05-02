package at.qe.skeleton.controller;

import at.qe.skeleton.exceptions.GameNotFoundException;
import at.qe.skeleton.exceptions.UserNotFoundException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.payload.response.SuccessResponse;
import at.qe.skeleton.repository.GameRepository;
import at.qe.skeleton.services.TeamService;
import at.qe.skeleton.services.TopicService;
import at.qe.skeleton.services.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.nio.file.AccessDeniedException;
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
    private UserService userService;

    @PostMapping(value = "/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> createTeam(@Valid @RequestBody TeamDto newTeam) throws ParseException, TeamService.TeamExistsException {
        Optional<Game> game = gameRepository.findById(newTeam.getGame_id());
        if(!game.isPresent()) throw new GameNotFoundException(newTeam.getGame_id());

        Team team = convertToTeamEntity(newTeam);
        team.setGame(game.get());

        TeamDto createdTeam = convertToTeamDto(teamService.addTeam(team));
        return new ResponseEntity<>((new SuccessResponse(createdTeam, 201)).toString(), HttpStatus.CREATED);
    }

    @PatchMapping("teams/{id}")
    public ResponseEntity<?> updateTeam(@RequestBody Map<Object, Object> fields, @PathVariable Long id, UriComponentsBuilder uriComponentsBuilder) throws ParseException, AccessDeniedException {
        Team existingTeam = teamService.findTeam(id);

        Optional<User> currentUser = userService.getAuthenticatedUser();
        if(!currentUser.isPresent()) throw new AccessDeniedException("Unauthenticated");
        if(!existingTeam.getUsers().contains(currentUser.get()) && currentUser.get().getRole() != UserRole.ROLE_MANAGER && currentUser.get().getRole() != UserRole.ROLE_ADMIN) throw new AccessDeniedException("Not part of this team");

        if(fields.containsKey("name")) {
            existingTeam.setName((String) fields.get("name"));
        }

        TeamDto team = convertToTeamDto(teamService.updateTeam(existingTeam));
        return ResponseEntity.ok((new SuccessResponse(team)).toString());
    }


    @GetMapping("/teams/{id}")
    private ResponseEntity<?> searchTeam(@PathVariable Long id) {
        TeamDto team = convertToTeamDto(teamService.findTeam(id));

        return ResponseEntity.ok((new SuccessResponse(team)).toString());
    }

    @GetMapping("/teams/{id}/users")
    private ResponseEntity<?> getAllTeamUsers(@PathVariable Long id) {
        Team team = teamService.findTeam(id);

        List<UserDto> users = team.getUsers().stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());

        return ResponseEntity.ok((new SuccessResponse(users)).toString());
    }

    @PostMapping("/teams/{id}/users")
    private ResponseEntity<?> addUserToTeam(@PathVariable Long id) throws AccessDeniedException {
        Team team = teamService.findTeam(id);
        Optional<User> user = userService.getAuthenticatedUser();
        if(!user.isPresent()) throw new AccessDeniedException("Unauthenticated");

        teamService.addUser(team, user.get());

        UserDto userDto = modelMapper.map(user, UserDto.class);

        return ResponseEntity.ok((new SuccessResponse(userDto, 201)).toString());
    }

    @DeleteMapping("/teams/{id}/users")
    private ResponseEntity<?> removeUserFromTeam(@PathVariable Long id) throws AccessDeniedException {
        Team team = teamService.findTeam(id);
        Optional<User> user = userService.getAuthenticatedUser();
        if(!user.isPresent()) throw new AccessDeniedException("Unauthenticated");

        teamService.removeUser(team, user.get());

        UserDto userDto = modelMapper.map(user, UserDto.class);

        return ResponseEntity.ok((new SuccessResponse(userDto, 201)).toString());
    }



    @GetMapping(value = "/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllTeams() {
        List<TeamDto> teams = teamService.findAllTeams().stream().map(this::convertToTeamDto).collect(Collectors.toList());

        return ResponseEntity.ok((new SuccessResponse(teams)).toString());
    }


    @DeleteMapping("/teams/{id}")
    private ResponseEntity<?> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(teamService.findTeam(id));

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