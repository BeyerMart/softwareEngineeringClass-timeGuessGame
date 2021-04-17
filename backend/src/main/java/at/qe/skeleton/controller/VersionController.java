package at.qe.skeleton.controller;

import at.qe.skeleton.Main;
import at.qe.skeleton.payload.response.SuccessResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping(value = "api/version", produces = MediaType.APPLICATION_JSON_VALUE)
public class VersionController {
    @GetMapping
    public String getVersion() throws JsonProcessingException {
        //Create json object
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("version", "v1.0");

        return (new SuccessResponse(node)).toString();
    }
}
