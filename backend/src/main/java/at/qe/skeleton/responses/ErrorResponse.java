package at.qe.skeleton.responses;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ErrorResponse {
    private String result;

    public ErrorResponse(String result, int status) {
        if(!((result.charAt(0) == '[' && result.charAt(result.length()-1) == ']') || (result.charAt(0) == '{' && result.charAt(result.length()-1) == '}'))) {
            //Escape that json is valid
            result = "'" + result +  "'";
        }
        this.result = "{'status':" + status + ",'error':" + result + ",'data':null,'timestamp':" + Instant.now().toString().substring(0, 23) + "Z}";
    }

    public ErrorResponse(String result) {
        this(result, 400);
    }

    @Override
    public String toString() {
        return result;
    }
}
