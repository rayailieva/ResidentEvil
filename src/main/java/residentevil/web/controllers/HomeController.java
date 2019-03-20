package residentevil.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends BaseController{

    @GetMapping("/")
    public ModelAndView home(){
        return super.view("index");
    }

    @GetMapping("/home")
    public ModelAndView userHome(){
        return super.view("home");
    }
}
