package residentevil.service;

import residentevil.domain.models.view.UserRoleViewModel;

import java.util.List;

public interface RoleService {

    List<UserRoleViewModel> extractAllRoles();
}
