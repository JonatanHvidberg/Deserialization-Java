package simple;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String role = "regular";

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
