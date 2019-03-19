package residentevil.domain.entities;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {

    private String authority;

    @Override
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
