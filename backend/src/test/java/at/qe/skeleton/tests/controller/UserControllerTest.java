package at.qe.skeleton.tests.controller;

import at.qe.skeleton.Main;
import at.qe.skeleton.model.User;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.security.jwt.JwtUtils;
import at.qe.skeleton.services.UserService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Main.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {
    private MockMvc mvc;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    User testUser = new User("testUser", "password", "mail@test.com");
    User testManager = new User("testManager", "password", "mail@test.com");
    User testAdmin = new User("testAdmin", "password", "mail@test.com");
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public void setup() {
        testUser.setRole(UserRole.ROLE_USER);
        testManager.setRole(UserRole.ROLE_MANAGER);
        testAdmin.setRole(UserRole.ROLE_ADMIN);
    }

    @BeforeEach
    public void setupContext() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void testNotSignedInUserGetUsers() throws Exception {
        List<User> users = new ArrayList<User>(Arrays.asList(testUser, testManager, testAdmin));
        Mockito.when(userService.getAllUsers()).thenReturn(users);
        MvcResult result = mvc.perform(get("/api/users").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains(testAdmin.getUsername()));
    }

    @Test
    public void testNotSignedInUserGetUser() throws Exception {
        Mockito.when(userService.getUserById(1000L)).thenReturn(Optional.of(testUser));
        mvc.perform(get("/api/users/1000")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username", is(testUser.getUsername())));
    }

    @Test
    @WithMockUser(authorities = {"MANAGER", "ADMIN"}, username = "manager")
    public void testAdminCreateUser() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Mockito.when(userService.createNewUser(Mockito.any(User.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        User localUser = testUser;
        localUser.setRole(null);
        String body = objectMapper.writeValueAsString(localUser);
        mvc.perform(post("/api/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.username", is(testUser.getUsername())));
    }

    @Test
    @WithMockUser(authorities = {"MANAGER", "ADMIN"})
    public void testPatchUserWithDifferentIdInputs() throws Exception {
        User existingUser = testUser;
        User localUser = testUser;
        Mockito.when(userService.getUserById(0L)).thenReturn(Optional.of(existingUser));
        localUser.setId(1000L);
        mvc.perform(patch("/api/users/0")
                .content(objectMapper.writeValueAsString(localUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(422));
    }

    @Test
    @WithMockUser(authorities = {"Manager", "Admin"})
    public void testPatchUserAsAdmin() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        User existingUser = testUser;
        existingUser.setId(0L);
        User newUser = new User(existingUser.getUsername(), "betterPassword", "betterTesting@mail.com");
        newUser.setId(0L);
        newUser.setRole(UserRole.ROLE_USER);
        Mockito.when(userService.getUserById(0L)).thenReturn(Optional.of(existingUser));
        Mockito.when(userService.updateUser(Mockito.any(User.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        mvc.perform(patch("/api/users/0")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email", is(newUser.getEmail())));
    }

    @Test
    @WithMockUser(authorities = {"Manager", "Admin", "User"})
    public void testDeleteUserAsAdmin() throws Exception {
        Mockito.when(userService.getUserById(0L)).thenReturn(Optional.of(testUser));
        Mockito.doAnswer((i) -> null).when(userService).deleteUser(testUser);
        mvc.perform(delete("/api/users/0"))
                .andExpect(status().isNoContent());
    }
}
