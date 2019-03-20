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
import residentevil.repository.UserRoleRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

   private final UserRepository userRepository;
   private final UserRoleRepository userRoleRepository;
   private final ModelMapper modelMapper;
   private final BCryptPasswordEncoder encoder;

   @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, ModelMapper modelMapper, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
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
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.userRepository
                .findByUsername(s)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    private void seedRolesInDb() {
        if (this.userRoleRepository.count() == 0) {
            UserRole userRole = new UserRole();
            userRole.setAuthority("USER");

            UserRole adminRole = new UserRole();
            adminRole.setAuthority("ADMIN");

            UserRole moderatorRole = new UserRole();
            moderatorRole.setAuthority("MODERATOR");

            this.userRoleRepository.save(userRole);
            this.userRoleRepository.save(adminRole);
            this.userRoleRepository.save(moderatorRole);

        }
    }

    private void giveRolesToUser(User user){
        if(this.userRepository.count() == 0){
            user.getAuthorities().add(this.userRoleRepository.findByAuthority("ADMIN"));
            user.getAuthorities().add(this.userRoleRepository.findByAuthority("MODERATOR"));
            user.getAuthorities().add(this.userRoleRepository.findByAuthority("USER"));
        }else {
            user.getAuthorities().add(this.userRoleRepository.findByAuthority("USER"));
        }
    }
}
