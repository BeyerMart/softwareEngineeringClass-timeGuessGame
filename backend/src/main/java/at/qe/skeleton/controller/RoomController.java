package at.qe.skeleton.controller;

import at.qe.skeleton.exceptions.RoomNotFoundException;
import at.qe.skeleton.exceptions.UserNotFoundException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.payload.response.ErrorResponse;
import at.qe.skeleton.payload.response.SuccessResponse;
import at.qe.skeleton.payload.response.websocket.WSResponseType;
import at.qe.skeleton.payload.response.websocket.WebsocketResponse;
import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.UserService;
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

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoomController {

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private RoomService roomService;

    @Autowired
    UserService userService;

    @Autowired
    VirtualUserController virtualUserController;

    @Autowired
    private CubeController cubeController;

    @Autowired
    private UserController userController;


    @PostMapping(value = "/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> createRoom(@RequestBody(required = false) Room roomRequest) {
        Room room;
        if (roomRequest == null)
            room = roomService.createNewRoom();
        else
            room = roomService.createNewRoom(roomRequest);
        return new ResponseEntity<>((new SuccessResponse(room, 201)).toString(), HttpStatus.CREATED);
    }

    @GetMapping("/rooms")
    public ResponseEntity<?> getAllRooms() {
        Map<Long, Room> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(new SuccessResponse(rooms).toString());
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<?> getRoom(@PathVariable Long id) {
        Room room = roomService.getRoomById(id).orElseThrow(() -> (new RoomNotFoundException(id)));
        return ResponseEntity.ok(new SuccessResponse(room).toString());
    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable("id") Long id) {
        roomService.deleteRoom(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/rooms/{id}")
    public ResponseEntity<?> updateRoom(@RequestBody Map<Object, Object> fields, @PathVariable("id") Long id, UriComponentsBuilder uriComponentsBuilder) {
        Room existingRoom = roomService.getRoomById(id).orElseThrow(() -> new RoomNotFoundException(id));

        if (fields.containsKey("id") && !Long.valueOf((Integer) fields.get("id")).equals(id)) {
            return ResponseEntity
                    .status(422)
                    .body((new ErrorResponse("Invalid field: id", 422)).toString());
        }

        fields.forEach((k, v) -> {
            switch ((String) k) {
                case "id":
                case "players":
                case "teams":
                case "game_id":
                    break;
                case "topic_id":
                    v = Long.parseLong((String) v);
                default:
                    Field field = ReflectionUtils.findField(Room.class, (String) k);
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, existingRoom, v);
            }
        });
        return ResponseEntity.ok((new SuccessResponse(existingRoom)).toString());
    }

    @PostMapping("/rooms/{id}/users")
    public ResponseEntity<?> joinRoom(@RequestBody(required = false) VirtualUserDto virtualUserDto, @PathVariable Long id) throws ParseException {

        if (virtualUserDto != null) {
            virtualUserDto.setCreator_id(0L); // Only temporary, will be overwritten in Room Service
            roomService.joinRoom(id, virtualUserController.convertToEntity(virtualUserDto));
        } else {
            roomService.joinRoom(id);
        }
        return ResponseEntity.ok((new SuccessResponse(roomService.getRoomById(id).get())).toString());
    }

    @GetMapping(value = "/rooms/{id}/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPlayersInRoom(@PathVariable Long id) {
        ArrayList<Object> players = new ArrayList<>();
        Room room = roomService.getRoomById(id).orElseThrow(() -> new RoomNotFoundException(id));
        players.addAll(getUsersFromSet(new HashSet<>(room.getPlayers().values())));
        players.addAll(getVirtualUsersFromSet(new HashSet<>(room.getPlayers().values())));
        return ResponseEntity.ok((new SuccessResponse(players)).toString());
    }

    @GetMapping(value = "/rooms/{id}/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getVirtualTeams(@PathVariable Long id) {
        Room room = roomService.getRoomById(id).orElseThrow(() -> new RoomNotFoundException(id));
        List<VirtualTeamDto> teams = room.getTeams().values().stream().map(virtualTeam -> new VirtualTeamDto(virtualTeam.getName(), new ArrayList<>(getPlayersFromSet(virtualTeam.getPlayers())))).collect(Collectors.toList());
        return ResponseEntity.ok((new SuccessResponse(teams)).toString());
    }

    @DeleteMapping("/rooms/{roomId}/users/{userId}")
    public ResponseEntity<?> userLeaveRoom(@RequestBody(required = false) VirtualUserDto virtualUserDto, @PathVariable Long roomId, @PathVariable(required = false) Long userId) throws ParseException {
        if (userId == null)
            userId = userService.getAuthenticatedUser().get().getId();
        if (virtualUserDto == null)
            roomService.removePlayer(userId, roomId);
        else
            roomService.removePlayer(roomId, virtualUserController.convertToEntity(virtualUserDto));
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/rooms/{roomId}/teams")
    public ResponseEntity<?> joinTeam(@PathVariable Long roomId, @RequestBody VirtualTeam team) {
        VirtualTeam virtualTeam = roomService.joinTeam(roomId, team.getName());
        return ResponseEntity.ok((new SuccessResponse(virtualTeam)).toString());
    }

    @DeleteMapping("/rooms/{roomId}/teams")
    public ResponseEntity<?> leaveTeam(@PathVariable Long roomId, @RequestBody VirtualTeam team) {
        if (roomService.leaveTeam(roomId, team.getName()))
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public void roomCreated(Room room) {
        template.convertAndSend("/rooms", (new WebsocketResponse(room, WSResponseType.ROOM_CREATED)).toString());
    }

    public void roomDeleted(Room room) {
        template.convertAndSend("/rooms", (new WebsocketResponse(room, WSResponseType.ROOM_DELETED)).toString());
        template.convertAndSend("/rooms/" + room.getId(), (new WebsocketResponse(room, WSResponseType.ROOM_DELETED)).toString());
    }

    public void roomChanged(Room room) {
        template.convertAndSend("/rooms", (new WebsocketResponse(room, WSResponseType.ROOM_CHANGED)).toString());
        template.convertAndSend("/rooms/" + room.getId(), (new WebsocketResponse(room, WSResponseType.ROOM_CHANGED)).toString());
    }

    public void userJoinedRoom(VirtualUser virtualUser, Room room) {
        template.convertAndSend("/rooms/" + room.getId(), (new WebsocketResponse(virtualUserController.convertToDto(virtualUser), WSResponseType.USER_JOINED_ROOM)).toString());
    }

    public void userJoinedRoom(User user, Room room) {
        UserDto userDto = new ModelMapper().map(user, UserDto.class);
        template.convertAndSend("/rooms/" + room.getId(), (new WebsocketResponse(userDto, WSResponseType.USER_JOINED_ROOM)).toString());
    }

    public void userLeftRoom(User user, Room room) {
        UserDto userDto = new ModelMapper().map(user, UserDto.class);
        template.convertAndSend("/rooms/" + room.getId(), (new WebsocketResponse(userDto, WSResponseType.USER_LEFT_ROOM)).toString());
    }

    public void userLeftRoom(VirtualUser virtualUser, Room room) {
        template.convertAndSend("/rooms/" + room.getId(), (new WebsocketResponse(virtualUserController.convertToDto(virtualUser), WSResponseType.USER_LEFT_ROOM)).toString());
    }

    public void userJoinedTeam(UserIdVirtualUser user, VirtualTeam virtualTeam, Room room) {
        Map<String, Object> data = new HashMap<>();
        data.put("team", new VirtualTeamDto(virtualTeam.getName(), new ArrayList<>(getPlayersFromSet(virtualTeam.getPlayers()))));
        data.put("user", userController.convertToDto(userService.getUserById(user.getUser_id()).orElseThrow(() -> new UserNotFoundException(user.getUser_id()))));
        template.convertAndSend("/rooms/" + room.getId(), (new WebsocketResponse(data, WSResponseType.USER_JOINED_TEAM)).toString());
    }

    public void userLeftTeam(UserIdVirtualUser user, VirtualTeam virtualTeam, Room room) {
        Map<String, Object> data = new HashMap<>();
        data.put("team", new VirtualTeamDto(virtualTeam.getName(), new ArrayList<>(getPlayersFromSet(virtualTeam.getPlayers()))));
        data.put("user", userController.convertToDto(userService.getUserById(user.getUser_id()).orElseThrow(() -> new UserNotFoundException(user.getUser_id()))));
        template.convertAndSend("/rooms/" + room.getId(), (new WebsocketResponse(data, WSResponseType.USER_LEFT_TEAM)).toString());
    }

    @PostMapping("/rooms/{id}/connect_pi")
    public ResponseEntity<?> connectPiToRoom(@RequestBody Map<String, Object> requestBody, @PathVariable String id){
        String piName = (String) requestBody.get("piName");
        roomService.connectRoomAndPi(Long.valueOf(id), piName);
        cubeController.setRoomOfPiName(piName, Integer.parseInt(id));
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/rooms/{id}/disconnect_pi")
    public ResponseEntity<?> disconnectPiFromRoom(@RequestBody String piName, @PathVariable String id) {
        roomService.disconnectRoomAndPi(Long.valueOf(id), piName);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/rooms/{id}/searchCube")
    public ResponseEntity<?> searchBlueToothCube(@PathVariable String id) {
        Room room = roomService.getRoomById(Integer.parseInt(id)).get();
        cubeController.cubeStartSearching(room);
        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }

    //WS Messages to the FrontEnd
    public void cubeNotConnected(int roomIdOnPi) {
        Room backendRoom = roomService.getRoomById(roomIdOnPi).get();
        backendRoom.setConnectedWithPiAndCube(false);
        backendRoom.setCube(null);
        roomService.updateRoom(backendRoom);
        template.convertAndSend("/rooms/" + backendRoom.getId(), WSResponseType.NOT_CONNECTED.toString());
    }

    public void cubeConnected(int roomIdOnPi, Cube cube) {
        Room backendRoom = roomService.getRoomById(roomIdOnPi).get();
        backendRoom.setConnectedWithPiAndCube(true);
        backendRoom.setCube(cube);
        roomService.updateRoom(backendRoom);
        template.convertAndSend("/rooms/" + backendRoom.getId(), WSResponseType.FOUND_AND_CONNECTED.toString());
    }

    private Set<VirtualUser> getVirtualUsersFromSet(Set<UserIdVirtualUser> userIdVirtualUsers) {
        Set<VirtualUser> virtualUsers = new HashSet<>();
        userIdVirtualUsers.forEach(userIdVirtualUser -> {
            virtualUsers.addAll(userIdVirtualUser.getVirtualUsers().values());
        });
        return virtualUsers;
    }

    private Set<UserDto> getUsersFromSet(Set<UserIdVirtualUser> userIdVirtualUsers) {
        return userIdVirtualUsers.stream()
                .map(userIdVirtualUser -> userController.convertToDto(userService.getUserById(userIdVirtualUser.getUser_id()).orElseThrow(() -> new UserNotFoundException(userIdVirtualUser.getUser_id())))).collect(Collectors.toSet());
    }

    private Set<Object> getPlayersFromSet(Set<UserIdVirtualUser> userIdVirtualUsers) {
        HashSet<Object> players = new HashSet<Object>(getVirtualUsersFromSet(userIdVirtualUsers));
        players.addAll(getUsersFromSet(userIdVirtualUsers));
        return players;
    }
}