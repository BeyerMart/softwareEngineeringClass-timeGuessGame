package at.qe.skeleton.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class RedirectController {
    // Forwards all routes to FrontEnd except: '/', '/index.html', '/api', '/api/**'
    // Required because of 'mode: history' usage in frontend routing.
    @RequestMapping(value = "{_:^(?!index\\.html|api|h2-console|v3|swagger-ui\\.html|swagger-ui).*$}", method = RequestMethod.GET)
    public ModelAndView redirectApi() {
        return new ModelAndView("forward:/");
    }
}
