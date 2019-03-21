package residentevil.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import residentevil.domain.entities.UserRole;
import residentevil.domain.models.view.UserRoleViewModel;
import residentevil.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;
    private ModelMapper modelMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserRoleViewModel> extractAllRoles() {
        List<UserRole> rolesFromDb = this.roleRepository.findAll();
        List<UserRoleViewModel> roleViewModels = new ArrayList<>();

        for (UserRole userRole : rolesFromDb) {

            UserRoleViewModel userRoleViewModel = this.modelMapper.map(userRole, UserRoleViewModel.class);

            roleViewModels.add(userRoleViewModel);
        }

        return roleViewModels;
    }
}
