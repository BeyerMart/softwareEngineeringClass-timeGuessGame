package at.qe.skeleton.services;

import at.qe.skeleton.controller.RoomController;
import at.qe.skeleton.exceptions.RoomNotFoundException;
import at.qe.skeleton.exceptions.TeamNotFoundException;
import at.qe.skeleton.exceptions.TopicNotFoundException;
import at.qe.skeleton.exceptions.UserNotFoundException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class RoomService {

    @Autowired
    UserService userService;

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    RoomController roomController;

    @Autowired
    TopicRepository topicRepository;

    private ConcurrentHashMap<Long, Room> rooms = new ConcurrentHashMap<Long, Room>();
    private long counter = 1;

    /**
     * Creates a new room and automatically makes the executing user join and host.
     *
     * @return the newly created room
     */
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public Room createNewRoom() {
        long hostId = userService.getAuthenticatedUser().get().getId();
        removePlayer(hostId);
        Room newRoom = new Room(counter++, hostId);
        rooms.put(newRoom.getId(), newRoom);
        roomController.roomCreated(newRoom);
        return newRoom;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public Room createNewRoom(Room roomRequest) {
        long hostId = userService.getAuthenticatedUser().get().getId();
        removePlayer(hostId);
        Room newRoom = new Room(counter++, hostId);
        if (roomRequest.getName() != null)
            newRoom.setName(roomRequest.getName());
        if (roomRequest.getTopic_id() != null) {
            if (!topicRepository.existsById(roomRequest.getTopic_id()))
                throw new TopicNotFoundException(roomRequest.getTopic_id());
            newRoom.setTopic_id(roomRequest.getTopic_id());
        }
        if (roomRequest.getMax_points() != 20) {
            newRoom.setMax_points(roomRequest.getMax_points());
        }
        rooms.put(newRoom.getId(), newRoom);
        roomController.roomCreated(newRoom);
        return newRoom;
    }

    public Map<Long, Room> getAllRooms() {
        return rooms;
    }

    public Optional<Room> getRoomById(long id) {
        Room get = rooms.get(id);
        if (get == null)
            return Optional.empty();
        return Optional.of(get);
    }

    /**
     * Method to override a room.
     * Can only be executed by the room host.
     *
     * @return updated room
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Room updateRoom(Room room) {
        if (userService.getAuthenticatedUser().get().getId() != rooms.get(room.getId()).getHost_id())
            throw new AccessDeniedException("not Room Host");
        rooms.put(room.getId(), room);
        roomController.roomChanged(room);
        return room;
    }

    /**
     * Removes a room.
     *
     * @param id roomId of room to remove
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void deleteRoom(long id) {
        Room room = rooms.get(id);
        if (room == null)
            throw new RoomNotFoundException(id);
        if (room.getHost_id() != userService.getAuthenticatedUser().get().getId())
            throw new AccessDeniedException("not Room Host");
        roomController.roomDeleted(rooms.remove(id));
    }

    /**
     * Removes a user and its virtual Users from all rooms.
     */
    private void removePlayer(long userId) {
        Iterator<Room> iterator = rooms.values().iterator();
        while (iterator.hasNext()) {
            Room room = iterator.next();
            if (room.getPlayers().remove(userId) != null) {
                if (room.getPlayers().isEmpty()) {
                    iterator.remove();
                    roomController.roomDeleted(room);
                } else {
                    removeUserFromAllTeams(userId, room);
                    room.checkHost();
                    roomController.roomChanged(room);
                }

            }
        }
    }

    /**
     * This Method deletes all empty rooms.
     * Triggers Socket ROOM_DELETED
     */
    public void deleteEmptyRooms() {
        rooms.values().removeIf(room -> {
            if (room.getPlayers().isEmpty()) {
                roomController.roomDeleted(room);
                return true;
            }
            return false;
        });
    }

    /**
     * Removes a given user from a given room.
     * Can be executed by the user himself or the host user.
     * Also removes the user from all the rooms teams.
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void removePlayer(Long userId, Long roomId) {
        Room room = rooms.get(roomId);
        if (room == null)
            throw new RoomNotFoundException(roomId);
        if (!userService.getAuthenticatedUser().get().getId().equals(room.getHost_id()) && !userService.getAuthenticatedUser().get().getId().equals(userId)) {
            throw new AccessDeniedException("Only the host can force a user leave");
        }
        if (room.getPlayers().remove(userId) == null) {
            throw new UserNotFoundException(userId);
        }
        if (room.getPlayers().isEmpty()) {
            rooms.remove(roomId);
            roomController.roomDeleted(room);
        } else {
            roomController.userLeftRoom(userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId)), room);
            if (userId.equals(room.getHost_id()))
                room.setHost_id(room.getPlayers().keySet().stream().findAny().get());
            roomController.roomChanged(room);
        }
        removeUserFromAllTeams(userId, room);
    }

    /**
     * Remove a virtual user from a room.
     * Can be executed by the parent user or the room host.
     *
     * @param roomId      id of the room the virtual user shall be removed from
     * @param virtualUser VirtualUser to remove, equals on ID is used to find the correct Object.
     */
    public void removePlayer(Long roomId, VirtualUser virtualUser) {
        Room room = rooms.get(roomId);
        if (room == null)
            throw new RoomNotFoundException(roomId);
        UserIdVirtualUser parentUser = room.getPlayers().get(virtualUser.getCreator_id());
        if (parentUser == null)
            throw new UserNotFoundException(virtualUser.getCreator_id());
        if (room.getHost_id() != userService.getAuthenticatedUser().get().getId() && !userService.getAuthenticatedUser().get().getId().equals(parentUser.getUser_id()))
            throw new AccessDeniedException("Only the host can force a user leave");
        if (!parentUser.getVirtualUsers().values().removeIf(virtualUserValue -> virtualUserValue.getVirtual_id().equals(virtualUser.getVirtual_id()) && virtualUserValue.getCreator_id().equals(virtualUser.getCreator_id())))
            throw new UserNotFoundException(virtualUser.getVirtual_id());
        roomController.roomChanged(room);
        roomController.userLeftRoom(virtualUser, room);
    }

    /**
     * Adds a virtual User to the room.
     * The Parent must be a part of the room already.
     *
     * @param roomId      Id of the Room that the VirtualUser is added to
     * @param virtualUser VirtualUSer to add
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void joinRoom(Long roomId, VirtualUser virtualUser) {
        Long executingUserId = userService.getAuthenticatedUser().get().getId();
        Room room = getRoomById(roomId).orElseThrow(() -> new RoomNotFoundException(roomId));
        virtualUser.setVirtual_id(VirtualUser.getNextId());
        virtualUser.setCreator_id(executingUserId);
        virtualUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        UserIdVirtualUser user = room.getPlayers().get(executingUserId);
        if (user == null)
            throw new AccessDeniedException("Parent is no member of the room");
        usernameExists(virtualUser.getUsername());
        user.getVirtualUsers().put(virtualUser.getVirtual_id(), virtualUser);
        roomController.roomChanged(room);
        roomController.userJoinedRoom(virtualUser, room);
    }

    /**
     * Lets a user join a room. Before that he is removed from all other rooms.
     *
     * @param roomId room to remove the user from
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void joinRoom(Long roomId) {
        User user = userService.getAuthenticatedUser().get();
        Long userId = user.getId();
        removePlayer(userId);
        Room room = getRoomById(roomId).orElseThrow(() -> new RoomNotFoundException(roomId));
        room.getPlayers().put(userId, new UserIdVirtualUser(userId));
        roomController.roomChanged(room);
        roomController.userJoinedRoom(user, room);
    }

    private void removeUserFromAllTeams(Long userId, Room room) {
        Iterator<VirtualTeam> iterator = room.getTeams().values().iterator();
        while (iterator.hasNext()) {
            VirtualTeam virtualTeam = iterator.next();
            Iterator<UserIdVirtualUser> playerIterator = virtualTeam.getPlayers().iterator();
            UserIdVirtualUser removedUser = null;
            while (playerIterator.hasNext()) {
                UserIdVirtualUser user = playerIterator.next();
                if (user.getUser_id().equals(userId)) {
                    removedUser = user;
                    playerIterator.remove();
                }
            }
            if (removedUser != null)
                roomController.userLeftTeam(removedUser, virtualTeam, room);
            if (virtualTeam.getPlayers().isEmpty() && room.getGame_id() < 0) {
                iterator.remove();
                roomController.roomChanged(room);
            }
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public VirtualTeam joinTeam(Long roomId, String teamName) {
        Room room = rooms.get(roomId);
        if (room == null)
            throw new RoomNotFoundException(roomId);
        UserIdVirtualUser user = room.getPlayers().get(userService.getAuthenticatedUser().get().getId());
        if (user == null)
            throw new AccessDeniedException("User is not part of the teams room");
        removeUserFromAllTeams(user.getUser_id(), room);
        VirtualTeam team = room.getTeams().get(teamName);
        if (team == null) {
            team = new VirtualTeam(teamName, user);
            room.getTeams().put(teamName, team);
        } else
            team.getPlayers().add(user);
        roomController.roomChanged(room);
        roomController.userJoinedTeam(user, team, room);
        return team;
    }

    /**
     * Method to leave a team. All virtual users leave the team with their parent user automatically.
     *
     * @param roomId   id of the room which should contain the team
     * @param teamName name of the team from which to remove
     * @return True if a user was removed, False if the user wasn't in the team before
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public boolean leaveTeam(long roomId, String teamName) {
        Room room = rooms.get(roomId);
        if (room == null)
            throw new RoomNotFoundException(roomId);
        VirtualTeam team = room.getTeams().get(teamName);
        if (team == null) {
            throw new TeamNotFoundException(teamName);
        }
        Long userId = userService.getAuthenticatedUser().get().getId();
        UserIdVirtualUser user = room.getPlayers().get(userId);
        if (team.getPlayers().removeIf(userIdVirtualUser -> userIdVirtualUser.getUser_id().equals(userId))) {
            if (team.getPlayers().isEmpty() && room.getGame_id() >= 0)
                room.getTeams().remove(teamName);
            roomController.roomChanged(room);
            roomController.userLeftTeam(user, team, room);
            return true;
        }
        return false;
    }

    private void usernameExists(String username) {
        if (userService.getUserByUsername(username).isPresent() || rooms.values().stream().anyMatch(room -> room.getPlayers().values().stream().anyMatch(userIdVirtualUser -> userIdVirtualUser.getVirtualUsers().values().stream().anyMatch(virtualUser -> virtualUser.getUsername().equals(username)))))
            throw new EntityExistsException(username);
    }

    private void usernameExists(String username, Room room) {
        if (userService.getUserByUsername(username).isPresent() || room.getPlayers().values().stream().anyMatch(userIdVirtualUser -> userIdVirtualUser.getVirtualUsers().values().stream().anyMatch(virtualUser -> virtualUser.getUsername().equals(username))))
            throw new EntityExistsException(username);
    }

        @PreAuthorize("hasAuthority('ROLE_USER')")
    public void connectRoomAndPi(Long roomId, String piName) {
        User authenticatedUser = userService.getAuthenticatedUser().get();
        if (authenticatedUser.getId() != getRoomById(roomId).get().getHost_id() && authenticatedUser.getRole() != UserRole.ROLE_ADMIN){
            throw new AccessDeniedException("Only the Host (or an admin) can connect the pi to a room");
        }
        Room room = getRoomById(roomId).orElseThrow(() -> new RoomNotFoundException(roomId));
        room.setPi_name(piName);
        roomController.roomChanged(room);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void randomUpdateCube(Long roomId){
        Room room = getRoomById(roomId).orElseThrow(() -> new RoomNotFoundException(roomId));
        Cube cube = new Cube();
        cube.setRoomId(Math.toIntExact(roomId));
        cube.setPiName(room.getPi_name());
        cube.setFacet((int) (Math.random()*12));
        cube.setBatteryLevel((int) (Math.random()*100));
        updateCube(roomId, cube);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void disconnectRoomAndPi(Long roomId, String piName) {
        Long userId = userService.getAuthenticatedUser().get().getId();
        Room room = getRoomById(roomId).orElseThrow(() -> new RoomNotFoundException(roomId));
        room.setPi_name(null);
        roomController.roomChanged(room);
    }

    public void updateCube(long roomId, Cube cube) {
        Optional<Room> optionalRoom = getRoomById(roomId);
        if (optionalRoom.isPresent()) {
            optionalRoom.get().setCube(cube);
            roomController.roomChanged(optionalRoom.get());
        }
    }


}