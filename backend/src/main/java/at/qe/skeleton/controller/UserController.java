package at.qe.skeleton.controller;

import at.qe.skeleton.exceptions.UserNotFoundException;
import at.qe.skeleton.model.User;
import at.qe.skeleton.model.UserDto;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.payload.response.SuccessResponse;
import at.qe.skeleton.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController()
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> all() {
        List<UserDto> users = userService.getAllUsers().stream().map(this::convertToDto).collect(Collectors.toList());

        return ResponseEntity.ok((new SuccessResponse(users)).toString());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> one(@PathVariable Long id) {
        UserDto user = userService.getUserById(id).map(this::convertToDto).orElseThrow(() -> new UserNotFoundException(id));

        return ResponseEntity.ok((new SuccessResponse(user)).toString());
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> newUser(@RequestBody UserDto newUser) throws UserService.UsernameExistsException, UserService.EmailExistsException, ParseException {
        if (newUser.getRole() == null) {
            UserRole defaultUserRoles = UserRole.ROLE_USER;
            newUser.setRole(defaultUserRoles);
        }
        UserDto user = convertToDto(userService.createNewUser(convertToEntity(newUser)));
        return ResponseEntity.ok((new SuccessResponse(user, 201)).toString());
    }

    @PatchMapping("users/{id}")
    public ResponseEntity<?> updateUser(@RequestBody Map<Object, Object> fields, @PathVariable Long id, UriComponentsBuilder uriComponentsBuilder) throws ParseException {
        User existingUser = userService.getUserById(id).get();

        fields.forEach((k, v) -> {
            if (!k.equals("role")) {
                Field field = ReflectionUtils.findField(User.class, (String) k);
                field.setAccessible(true);
                ReflectionUtils.setField(field, existingUser, v);
            } else {
                existingUser.setRole(UserRole.valueOf((String) v));
            }
        });
        UserDto user = convertToDto(userService.updateUser(existingUser));
        return ResponseEntity.ok((new SuccessResponse(user)).toString());
    }

    @DeleteMapping(value = "/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(userService.getUserById(id).orElseThrow(() -> new UserNotFoundException(id)));

        return ResponseEntity.ok((new SuccessResponse(null, 204)).toString());
    }

    private UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private User convertToEntity(UserDto userDto) throws ParseException {
        return modelMapper.map(userDto, User.class);
    }
}
