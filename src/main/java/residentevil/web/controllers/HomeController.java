package residentevil.web.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class HomeController extends BaseController{

    @GetMapping("/")
    public ModelAndView index(){
        return super.view("index");
    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView home(Principal principal, ModelAndView modelAndView) {

        modelAndView.addObject("username", principal.getName());

        return super.view("home", modelAndView);
    }
}
