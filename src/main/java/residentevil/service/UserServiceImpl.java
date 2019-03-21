package residentevil.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import residentevil.domain.entities.User;
import residentevil.domain.entities.UserRole;
import residentevil.domain.models.service.UserServiceModel;
import residentevil.repository.UserRepository;
import residentevil.repository.RoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

   private final UserRepository userRepository;
   private final RoleRepository roleRepository;
   private final ModelMapper modelMapper;
   private final BCryptPasswordEncoder encoder;

   @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.encoder = encoder;
   }

    @Override
    public boolean register(UserServiceModel userServiceModel) {
        this.seedRolesInDb();

        User user = this.modelMapper.map(userServiceModel, User.class);
        user.setPassword(this.encoder.encode(userServiceModel.getPassword()));
        this.giveRolesToUser(user);

        try{
            this.userRepository.saveAndFlush(user);

            return true;
        }catch (Exception e){
            e.printStackTrace();

            return false;
        }
    }

    @Override
    public List<UserServiceModel> findAllUsers() {
        return this.userRepository.findAll()
                .stream()
                .map(u -> this.modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.userRepository
                .findByUsername(s)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    private void seedRolesInDb() {
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

    private void giveRolesToUser(User user){
        if(this.userRepository.count() == 0){
            user.getAuthorities().add(this.roleRepository.findByAuthority("ADMIN"));
            user.getAuthorities().add(this.roleRepository.findByAuthority("MODERATOR"));
            user.getAuthorities().add(this.roleRepository.findByAuthority("USER"));
        }else {
            user.getAuthorities().add(this.roleRepository.findByAuthority("USER"));
        }
    }
}
