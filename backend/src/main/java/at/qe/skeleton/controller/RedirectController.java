package at.qe.skeleton.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class RedirectController implements ErrorController {
    private static final String PATH = "/error";
    // Forwards all routes to FrontEnd except: '/', '/index.html', '/api', '/api/**'
    // Required because of 'mode: history' usage in frontend routing.
    @RequestMapping(value = "{_:^(?!index\\.html|api).$}", method = RequestMethod.GET)
    public ModelAndView redirectApi() {
        return new ModelAndView("forward:/");
    }

    @RequestMapping(value = PATH)
    public ModelAndView error() {
        return new ModelAndView("forward:/");
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
