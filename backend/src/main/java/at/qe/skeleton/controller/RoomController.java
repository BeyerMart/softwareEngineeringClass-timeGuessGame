package at.qe.skeleton.controller;

import at.qe.skeleton.exceptions.RoomNotFoundException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.payload.response.ErrorResponse;
import at.qe.skeleton.payload.response.SuccessResponse;
import at.qe.skeleton.payload.response.websocket.WSResponseType;
import at.qe.skeleton.payload.response.websocket.WebsocketResponse;
import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.UserService;
import org.modelmapper.ModelMapper;
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
import java.util.Map;

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


    @PostMapping(value = "/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> createRoom() {
        Room room = roomService.createNewRoom();
        return new ResponseEntity<>((new SuccessResponse(room, 201)).toString(), HttpStatus.CREATED);
    }

    @GetMapping("/rooms")
    public ResponseEntity<?> getAllRooms() {
        Map<Long, Room> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(new SuccessResponse(rooms).toString());
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
            roomService.joinRoom(id, virtualUserController.convertToEntity(virtualUserDto));
        } else {
            roomService.joinRoom(id);
        }
        return ResponseEntity.ok((new SuccessResponse(roomService.getRoomById(id).get())).toString());
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
        VirtualTeam virtualTeam = roomService.joinTeam(roomId, team.getTeam_name());
        return ResponseEntity.ok((new SuccessResponse(virtualTeam)).toString());
    }

    @DeleteMapping("/rooms/{roomId}/teams")
    public ResponseEntity<?> leaveTeam(@PathVariable Long roomId, @RequestBody VirtualTeam team) {
        if (roomService.leaveTeam(roomId, team.getTeam_name()))
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public void roomCreated(Room room) {
        template.convertAndSend("/rooms", (new WebsocketResponse(room, WSResponseType.ROOM_CREATED)).toString());
    }

    public void roomDeleted(Room room) {
        template.convertAndSend("/rooms", (new WebsocketResponse(room, WSResponseType.ROOM_DELETED)).toString());
        template.convertAndSend("/rooms/" + room.getRoom_id(), (new WebsocketResponse(room, WSResponseType.ROOM_DELETED)).toString());
    }

    public void roomChanged(Room room) {
        template.convertAndSend("/rooms", (new WebsocketResponse(room, WSResponseType.ROOM_CHANGED)).toString());
        template.convertAndSend("/rooms/" + room.getRoom_id(), (new WebsocketResponse(room, WSResponseType.ROOM_CHANGED)).toString());
    }

    public void userJoinedRoom(VirtualUser virtualUser, Room room) {
        template.convertAndSend("/rooms/" + room.getRoom_id(), (new WebsocketResponse(virtualUserController.convertToDto(virtualUser), WSResponseType.USER_JOINED_ROOM)).toString());
    }

    public void userJoinedRoom(User user, Room room) {
        UserDto userDto = new ModelMapper().map(user, UserDto.class);
        template.convertAndSend("/rooms/" + room.getRoom_id(), (new WebsocketResponse(userDto, WSResponseType.USER_JOINED_ROOM)).toString());
    }

    public void userLeftRoom(User user, Room room) {
        UserDto userDto = new ModelMapper().map(user, UserDto.class);
        template.convertAndSend("/rooms/" + room.getRoom_id(), (new WebsocketResponse(userDto, WSResponseType.USER_LEFT_ROOM)).toString());
    }

    public void userLeftRoom(VirtualUser virtualUser, Room room) {
        template.convertAndSend("/rooms/" + room.getRoom_id(), (new WebsocketResponse(virtualUserController.convertToDto(virtualUser), WSResponseType.USER_LEFT_ROOM)).toString());
    }

    public void userJoinedTeam(UserIdVirtualUser user, Room room) {
        template.convertAndSend("/rooms/" + room.getRoom_id(), (new WebsocketResponse(user, WSResponseType.USER_JOINED_TEAM)).toString());
    }

    public void userLeftTeam(UserIdVirtualUser user, Room room) {
        template.convertAndSend("/rooms/" + room.getRoom_id(), (new WebsocketResponse(user, WSResponseType.USER_LEFT_TEAM)).toString());
    }

    @PostMapping("/rooms/{id}/connect_pi")
    public void connectPiToRoom(@RequestBody String piName, @PathVariable String id){
        roomService.connectRoomAndPi(Long.valueOf(id), piName);
    }

    @PostMapping("/rooms/{id}/disconnect_pi")
    public void disconnectPiFromRoom(@RequestBody String piName, @PathVariable String id){
        roomService.disconnectRoomAndPi(Long.valueOf(id), piName);
    }

    @PostMapping("/rooms/{id}/searchCube")
    public void searchBlueToothCube(@PathVariable String id){
        Room room = roomService.getRoomById(Integer.parseInt(id)).get();
        cubeController.cubeStartSearching(room);
    }

    //WS Messages to the FrontEnd
    //TODO FrontEnd shows "Cube not found / not connected. reset battery and retry"
    public void cubeNotConnected(Room room){
        room.setConnectedWithPiAndCube(false);
        template.convertAndSend("/rooms/" + room.getRoom_id(), WSResponseType.NOT_CONNECTED.toString());
    }
    public void cubeConnected(Room room, Cube cube){
        room.setConnectedWithPiAndCube(true);
        room.setCube(cube);
        template.convertAndSend("/rooms/" + room.getRoom_id(), WSResponseType.FOUND_AND_CONNECTED.toString());
    }

//    private RoomDto convertToRoomDto(Room room) {
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        return modelMapper.map(room, RoomDto.class);
//    }
//
//    private Room convertToRoomEntity(RoomDto roomDto) {
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        return modelMapper.map(roomDto, Room.class);
//    }
}