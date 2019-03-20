package residentevil.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import residentevil.domain.entities.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, String> {

    UserRole findByAuthority(String authority);
}
