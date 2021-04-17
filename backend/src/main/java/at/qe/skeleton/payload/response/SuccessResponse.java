package at.qe.skeleton.payload.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SuccessResponse {
    private String result;

    ObjectMapper mapper = new ObjectMapper();

    public SuccessResponse(Object input, int status) {
        String result = "";
        try {
            result = mapper.writeValueAsString(input);
        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }

        this.result = "{\"status\":" + status + ",\"error\":null,\"data\":" + result + ",\"timestamp\":\"" + Instant.now().toString().substring(0, 23) + "Z\"}";
    }

    public SuccessResponse(Object input) {
         this(input, 200);
    }

    @Override
    public String toString() {
        return result;
    }
}
