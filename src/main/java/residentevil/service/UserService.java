package residentevil.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import residentevil.domain.models.binding.UserEditBindingModel;
import residentevil.domain.models.service.UserServiceModel;

import java.util.List;

public interface UserService extends UserDetailsService {

    boolean register(UserServiceModel userServiceModel);

    List<UserServiceModel> findAllUsers();

    UserEditBindingModel extractUserForEditById(String id);

    boolean insertEditedUser(UserEditBindingModel userEditBindingModel);
}
