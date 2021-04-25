package at.qe.skeleton.controller;

import at.qe.skeleton.model.User;
import at.qe.skeleton.model.UserDto;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.payload.request.LoginRequest;
import at.qe.skeleton.payload.request.SignupRequest;
import at.qe.skeleton.payload.response.ErrorResponse;
import at.qe.skeleton.payload.response.JwtResponse;
import at.qe.skeleton.payload.response.SuccessResponse;
import at.qe.skeleton.security.jwt.JwtUtils;
import at.qe.skeleton.services.UserDetailsImpl;
import at.qe.skeleton.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    ModelMapper mapper = new ModelMapper();

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok((new SuccessResponse(new JwtResponse(jwt, userService.getUserById(userDetails.getId()).get()))).toString());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        User user = new User(signupRequest.getUsername(),
                passwordEncoder.encode(signupRequest.getPassword()),
                signupRequest.getEmail());

        user.setRole(UserRole.ROLE_USER);

        try {
            userService.createNewUser(user);
        } catch (UserService.UsernameExistsException e) {
            return ResponseEntity
                    .badRequest()
                    .body((new ErrorResponse("Username is already taken!", 400)).toString());
        } catch (UserService.EmailExistsException e) {
            return ResponseEntity
                    .badRequest()
                    .body((new ErrorResponse("Email is already in use!", 400)).toString());
        } catch (AccessDeniedException e) {
            return ResponseEntity
                    .badRequest()
                    .body((new ErrorResponse(e.getMessage(), 400)).toString());
        }

        return new ResponseEntity<>((new SuccessResponse(mapper.map(user, UserDto.class))).toString(), HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        User currentUser = userService.getAuthenticatedUser().get();
        return ResponseEntity.ok((new SuccessResponse(mapper.map(currentUser, UserDto.class))).toString());
    }
}
