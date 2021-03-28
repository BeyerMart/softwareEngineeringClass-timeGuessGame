package at.qe.skeleton.resttest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("api/version")
public class RestTestController {
    @GetMapping
    public String getVersion() {
        return "v 1.01";
    }
}