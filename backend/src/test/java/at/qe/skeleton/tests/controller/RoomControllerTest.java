package at.qe.skeleton.tests.controller;


import at.qe.skeleton.Main;
import at.qe.skeleton.model.*;
import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.TermService;
import at.qe.skeleton.services.TopicService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes= Main.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoomControllerTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private RoomService roomService;

    User testAdmin = new User("testAdmin", "password","mail@test.com");

    Room testRoom = new Room(-1, -1);

    @BeforeAll
    public void setup() {
        testAdmin.setRole(UserRole.ROLE_ADMIN);
        testAdmin.setId(-1L);

        testRoom.setName("heyX");
    }

    @BeforeEach
    public void setupContext() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void testCreateEmptyRoom() throws Exception {
        Mockito.when(roomService.createNewRoom()).thenReturn(testRoom);
        mvc.perform(post("/api/rooms").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

    @Test
    public void testCreateRoom() throws Exception {
        Mockito.when(roomService.createNewRoom(Mockito.any(Room.class))).thenReturn(testRoom);
        String body = "{\"name\":\"" + testRoom.getName() + "\",\"max_points\":564654}";
        mvc.perform(post("/api/rooms").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

    @Test
    public void testGetAllRooms() throws Exception {
        Map<Long, Room> resultMap = new HashMap<>();
        resultMap.put(testRoom.getId(), testRoom);
        Mockito.when(roomService.getAllRooms()).thenReturn(resultMap);
        MvcResult result = mvc.perform(get("/api/rooms").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testRoom.getName()));
    }

    @Test
    public void testGetRoom() throws Exception {
        Mockito.when(roomService.getRoomById(Mockito.any(Long.class))).thenReturn(Optional.of(testRoom));
        MvcResult result = mvc.perform(get("/api/rooms/{id}",testRoom.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testRoom.getName()));
    }

    @Test
    public void testDeleteRoom() throws Exception {
        Mockito.doAnswer((i) -> null).when(roomService).deleteRoom(testRoom.getId());

        mvc.perform(delete("/api/rooms/{id}",testRoom.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
    }

    @Test
    public void testJoinRoomVirtual() throws Exception {
        Mockito.doAnswer((i) -> null).when(roomService).joinRoom(Mockito.any(), Mockito.any(VirtualUser.class));
        Mockito.when(roomService.getRoomById(Mockito.any(Long.class))).thenReturn(Optional.of(testRoom));

        String body = "{\"username\": \"" + testAdmin.getUsername() + "\"}";
        mvc.perform(post("/api/rooms/{id}/users",testRoom.getId()).content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testUpdateRoom() throws Exception {
        Mockito.when(roomService.getRoomById(Mockito.any(Long.class))).thenReturn(Optional.of(testRoom));

        String body = "{\"name\": \"dsfkljsfdcalkd\"}";
        MvcResult result = mvc.perform(patch("/api/rooms/{id}",testRoom.getId()).content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("dsfkljsfdcalkd"));
    }

    @Test
    public void testBadUpdateRoom() throws Exception {
        Mockito.when(roomService.getRoomById(Mockito.any(Long.class))).thenReturn(Optional.of(testRoom));

        String body = "{\"id\": -10}";
        mvc.perform(patch("/api/rooms/{id}",testRoom.getId()).content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
    }


    @Test
    public void testJoinRoom() throws Exception {
        Mockito.doAnswer((i) -> null).when(roomService).joinRoom(Mockito.any());
        Mockito.when(roomService.getRoomById(Mockito.any(Long.class))).thenReturn(Optional.of(testRoom));

        mvc.perform(post("/api/rooms/{id}/users",testRoom.getId())).andExpect(status().isOk());
    }

    @Test
    public void testGetPlayersInRoom() throws Exception {
        Mockito.when(roomService.getRoomById(Mockito.any(Long.class))).thenReturn(Optional.of(testRoom));
        testRoom.setPlayers(new HashMap<>());
        mvc.perform(get("/api/rooms/{id}/users",testRoom.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void testGetTeamsInRoom() throws Exception {
        Mockito.when(roomService.getRoomById(Mockito.any(Long.class))).thenReturn(Optional.of(testRoom));
        mvc.perform(get("/api/rooms/{id}/teams",testRoom.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void testuserLeaveRoom() throws Exception {
        Mockito.when(roomService.getRoomById(Mockito.any(Long.class))).thenReturn(Optional.of(testRoom));
        mvc.perform(get("/api/rooms/{id}/teams",testRoom.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
    }

}
