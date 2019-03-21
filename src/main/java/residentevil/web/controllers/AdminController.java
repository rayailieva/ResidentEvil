package residentevil.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import residentevil.domain.models.view.UserListViewModel;
import residentevil.service.RoleService;
import residentevil.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminController extends BaseController{

    private final UserService userService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, ModelMapper modelMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ModelAndView listUsers(Principal principal, ModelAndView modelAndView) {
        List<UserListViewModel> userViewModels = this.userService.findAllUsers()
                .stream()
                .filter(u -> !u.getUsername().equals(principal.getName()))
                .map(user -> this.modelMapper.map(user, UserListViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("users", userViewModels);

        return super.view("users", modelAndView);
    }

}
