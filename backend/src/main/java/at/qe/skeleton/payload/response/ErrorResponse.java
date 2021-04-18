package at.qe.skeleton.payload.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ErrorResponse {
    private String error;

    ObjectMapper mapper = new ObjectMapper();

    public ErrorResponse(Object input, int status) {
        String error = "";
        try {
            error = mapper.writeValueAsString(input);
        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }

        this.error = "{\"status\":" + status + ",\"error\":" + error + ",\"data\":null,\"timestamp\":\"" + Instant.now().toString().substring(0, 23) + "Z\"}";
    }

    public ErrorResponse(Object input) {
        this(input, 400);
    }

    @Override
    public String toString() {
        return error;
    }
}
