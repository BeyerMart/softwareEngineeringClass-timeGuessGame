package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.TeamNotFoundException;
import at.qe.skeleton.model.Team;
import at.qe.skeleton.model.User;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.model.VirtualUser;
import at.qe.skeleton.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class TeamService {
    private final ConcurrentHashMap<Long, List<VirtualUser>> virtualUsers = new ConcurrentHashMap<>();

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserService userService;

    /**
     * @param team the team to create
     * @return newly created team
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Team addTeam(Team team) throws TeamExistsException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        team.setCreated_at(timestamp);
        team.setPoints(0L);

        return teamRepository.save(team);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ArrayList<Object> getAllTeamUsers(Team team) {
        ArrayList<Object> users = new ArrayList<>(Arrays.asList(team.getUsers().toArray()));

        if (virtualUsers.containsKey(team.getId())) {
            users.addAll(virtualUsers.get(team.getId()));
        }

        return users;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public VirtualUser addVirtualUser(Team team, VirtualUser virtualUser) {
        virtualUser.setVirtual_id(VirtualUser.getNextId());
        virtualUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        synchronized (virtualUsers) {
            List<VirtualUser> virtUserArr = virtualUsers.getOrDefault(team.getId(), new ArrayList<VirtualUser>());

            virtUserArr.add(virtualUser);
            virtualUsers.put(team.getId(), virtUserArr);
        }

        return virtualUser;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public VirtualUser removeVirtualUser(Team team, Long virtualUserId) {
        User authUser = userService.getAuthenticatedUser().get();
        synchronized (virtualUsers) {
            List<VirtualUser> virtUserArr = virtualUsers.getOrDefault(team.getId(), new ArrayList<VirtualUser>());

            List<VirtualUser> foundUser = virtUserArr.stream().filter(virtualUser -> virtualUser.getVirtual_id().equals(virtualUserId)).collect(Collectors.toList());
            if (foundUser.size() > 0) {
                if ((foundUser.get(0).getCreator_id() != authUser.getId()) && authUser.getRole() == UserRole.ROLE_USER)
                    throw new AccessDeniedException("Insufficient Authority");
            }

            //Remove duplicates (which should not exist)
            virtUserArr = virtUserArr.stream().filter(virtualUser -> !virtualUser.getVirtual_id().equals(virtualUserId)).collect(Collectors.toList());

            if (virtUserArr.size() == 0) {
                virtualUsers.remove(team.getId());
            } else {
                virtualUsers.put(team.getId(), virtUserArr);
            }

            return (foundUser.size() > 0) ? foundUser.get(0) : null;
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Team addUser(Team team, User user) {
        if (team.getUsers() == null)
            team.setUsers(new HashSet<User>());
        team.getUsers().add(user);
        return teamRepository.save(team);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Team removeUser(Team team, User user) {
        team.getUsers().remove(user);

        synchronized (virtualUsers) {
            List<VirtualUser> virtUserArr = virtualUsers.getOrDefault(team.getId(), new ArrayList<VirtualUser>());
            virtUserArr = virtUserArr.stream().filter(virtualUser -> virtualUser.getCreator_id() != user.getId()).collect(Collectors.toList());

            if (virtUserArr.size() == 0) {
                virtualUsers.remove(team.getId());
            } else {
                virtualUsers.put(team.getId(), virtUserArr);
            }
        }

        Team result = teamRepository.save(team);
        if (result.getUsers().size() == 0) {
            virtualUsers.remove(team.getId());
            teamRepository.delete(result);
            return null;
        }

        return result;
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
     *
     * @param id id of searched team
     * @return searched team
     * @throws TeamNotFoundException
     */
    public Optional<Team> findTeam(Long id) throws TeamNotFoundException {
        if (!teamRepository.existsById(id)) {
            throw (new TeamNotFoundException(id));
        }
        return teamRepository.findById(id);
    }

    /**
     * Find all existing teams
     *
     * @return all teams
     */
    public List<Team> findAllTeams() {
        return teamRepository.findAll();
    }

    /**
     * @param team team to be deleted
     * @return httpStatusNOCONTENT
     * @throws TeamNotFoundException
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteTeam(Team team) {
        if (!teamRepository.existsById(team.getId())) {
            throw (new TeamNotFoundException(team.getId()));
        }

        //Remove virtual users if exist
        virtualUsers.remove(team.getId());

        teamRepository.delete(team);
    }

    public class TeamExistsException extends Exception {
    }
}