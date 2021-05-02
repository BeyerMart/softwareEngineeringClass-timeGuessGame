package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.TeamNotFoundException;
import at.qe.skeleton.model.Team;
import at.qe.skeleton.model.User;
import at.qe.skeleton.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TeamService {
    public class TeamExistsException extends Exception {}

    @Autowired
    private TeamRepository teamRepository;

    /**
     * @param team the team to create
     * @return newly created team
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Team addTeam (Team team) throws TeamExistsException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        team.setCreated_at(timestamp);
        team.setPoints(0L);

        return teamRepository.save(team);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Team addUser(Team team, User user) {
        team.getUsers().add(user);
        return teamRepository.save(team);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Team removeUser(Team team, User user) {
        team.getUsers().remove(user);
        return teamRepository.save(team);
    }

    /**
     * Updates a team.
     *
     * @param team the team to update
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Team updateTeam(Team team) {
        return teamRepository.save(team);
    }


    /**
     * Find one specific team by ID
     * @throws TeamNotFoundException
     * @param id id of searched team
     * @return searched team
     */
    public Team findTeam(Long id) throws TeamNotFoundException {
        if(!teamRepository.existsById(id)){
            throw (new TeamNotFoundException(id));
        }
        return teamRepository.getOne(id);
    }


    /**
     * Find all existing teams
     * @return all teams
     */
    public List<Team> findAllTeams() {
        return teamRepository.findAll();
    }

    /**
     * @throws TeamNotFoundException
     * @param team team to be deleted
     * @return httpStatusNOCONTENT
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void deleteTeam(Team team){
        if(!teamRepository.existsById(team.getId())){
            throw (new TeamNotFoundException(team.getId()));
        }
        teamRepository.delete(team);
    }
}