package residentevil.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import residentevil.domain.models.service.UserServiceModel;

public interface UserService extends UserDetailsService {

    boolean register(UserServiceModel userServiceModel);

}
