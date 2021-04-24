package at.qe.skeleton.tests.controller;

import at.qe.skeleton.Main;
import at.qe.skeleton.model.User;
import at.qe.skeleton.payload.response.JwtResponse;
import at.qe.skeleton.repository.UserRepository;
import at.qe.skeleton.security.jwt.JwtUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes= Main.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerTest {
    private MockMvc mvc;
    private String username;
    private String password;
    private String token;
    private String email;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private WebApplicationContext context;

    @BeforeAll
    public void setup() {
        username = UUID.randomUUID().toString().substring(0,20);
        password = UUID.randomUUID().toString().substring(0,20);
        email = UUID.randomUUID().toString().substring(0,10) + "@" + UUID.randomUUID().toString().substring(0,6) + ".de";;
    }

    @BeforeEach
    public void setupContext() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    public void testSuccessfulSignup() throws Exception {
        String body = "{\"username\":\"" + username + "\",\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
        MvcResult result = mvc.perform(post("/api/auth/signup").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
    }

    @Test
    @Order(2)
    public void testBadSignup() throws Exception {
        //Check if email is not provided
        String body = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";
        MvcResult result = mvc.perform(post("/api/auth/signup").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

        //Check if username is not provided
        body = "{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}";
        result = mvc.perform(post("/api/auth/signup").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @Order(2)
    public void testDuplicateSignup() throws Exception {
        String body = "{\"username\":\"" + username + "\",\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
        MvcResult result = mvc.perform(post("/api/auth/signup").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @Order(2)
    public void testSuccessfulSignin() throws Exception {
        String body = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        MvcResult result = mvc.perform(post("/api/auth/signin").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        //Parse JWT token for later tests
        JSONObject node = new JSONObject(result.getResponse().getContentAsString());
        JSONObject data = node.getJSONObject("data");

        String token = data.getString("token");
        assertTrue(jwtUtils.validateJwtToken(token));

        this.token = token;
    }

    @Test
    @Order(2)
    public void testSigninWithoutPassword() throws Exception {
        String body = "{\"username\":\"" + username + "\",\"password\":\"\"}"; //Bad = empty password
        MvcResult result = mvc.perform(post("/api/auth/signin").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @Order(2)
    public void testBadSignin() throws Exception {
        String body = "{\"username\":\"" + username + "\",\"password\":\"foobar\"}";
        MvcResult result = mvc.perform(post("/api/auth/signin").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    @Order(3)
    public void testSuccessfulCurrentUser() throws Exception {
        MvcResult result = mvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + token)).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();

        assertTrue(response.contains(username));
    }

    @Test
    @Order(3)
    public void testBadCurrentUser() throws Exception {
        MvcResult result = mvc.perform(get("/api/auth/me").header("Authorization", "Bearer johndoe")).andExpect(status().isUnauthorized()).andReturn();
    }

    @AfterAll
    public void teardown() {
        User user = userRepository.findByUsername(username).get();
        userRepository.delete(user);
    }
}