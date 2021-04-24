package at.qe.skeleton.payload.response;

import at.qe.skeleton.model.User;
import at.qe.skeleton.model.UserDto;
import org.modelmapper.ModelMapper;

public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private UserDto user;

    public JwtResponse() {}

    ModelMapper mapper = new ModelMapper();;

    public JwtResponse(String token, User user) {
        this.token = token;
        this.user = mapper.map(user, UserDto.class);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

   public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}

