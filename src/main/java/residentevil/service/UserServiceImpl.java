package residentevil.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import residentevil.domain.entities.User;
import residentevil.domain.entities.UserRole;
import residentevil.domain.models.binding.UserEditBindingModel;
import residentevil.domain.models.binding.UserRegisterBindingModel;
import residentevil.domain.models.service.UserServiceModel;
import residentevil.repository.UserRepository;
import residentevil.repository.RoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean register(UserServiceModel userServiceModel) {

        User user = this.modelMapper.map(userServiceModel, User.class);
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));

        this.insertUserRoles();

        if (this.userRepository.count() == 0) {
            user.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_ADMIN"));
            user.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_MODERATOR"));
            user.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_USER"));
        } else {
            user.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_USER"));
        }

        this.userRepository.save(user);
        return true;
    }

    @Override
    public List<UserServiceModel> findAllUsers() {
        return this.userRepository.findAll()
                .stream()
                .map(u -> this.modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Override
    public UserEditBindingModel extractUserForEditById(String id) {
        User userFromDb = this.userRepository.findById(id).orElse(null);

        if (userFromDb == null) {
            throw new IllegalArgumentException("Non-existent user.");
        }

        UserEditBindingModel userBindingModel = this.modelMapper.map(userFromDb, UserEditBindingModel.class);

        for (UserRole userRole : userFromDb.getAuthorities()) {
            userBindingModel.getRoleAuthorities().add(userRole.getAuthority());
        }

        return userBindingModel;
    }

    @Override
    public boolean insertEditedUser(UserEditBindingModel userEditBindingModel) {
        User user = this.userRepository.findByUsername(userEditBindingModel.getUsername()).orElse(null);
        user.getAuthorities().clear();

        if (userEditBindingModel.getRoleAuthorities().contains("ADMIN")) {
            user.getAuthorities().add(this.roleRepository.findByAuthority("USER"));
            user.getAuthorities().add(this.roleRepository.findByAuthority("MODERATOR"));
            user.getAuthorities().add(this.roleRepository.findByAuthority("ADMIN"));
        } else if (userEditBindingModel.getRoleAuthorities().contains("MODERATOR")) {
            user.getAuthorities().add(this.roleRepository.findByAuthority("USER"));
            user.getAuthorities().add(this.roleRepository.findByAuthority("MODERATOR"));
        } else {
            user.getAuthorities().add(this.roleRepository.findByAuthority("USER"));
        }

        this.userRepository.save(user);

        return true;
    }

    private void insertUserRoles() {
        if (this.roleRepository.count() == 0) {
            UserRole userRole = new UserRole();
            userRole.setAuthority("ROLE_USER");

            UserRole adminRole = new UserRole();
            adminRole.setAuthority("ROLE_ADMIN");

            UserRole moderatorRole = new UserRole();
            moderatorRole.setAuthority("ROLE_MODERATOR");

            this.roleRepository.save(userRole);
            this.roleRepository.save(adminRole);
            this.roleRepository.save(moderatorRole);

        }
    }
}