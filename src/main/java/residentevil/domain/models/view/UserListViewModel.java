package residentevil.domain.models.view;

import java.util.List;

public class UserListViewModel {

    private String id;
    private String username;
    private String authorities;

    public UserListViewModel(){}

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }
}
