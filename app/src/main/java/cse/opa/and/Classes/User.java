package cse.opa.and.Classes;

/**
 * Created by Peter on 28/3/2018.
 */

public class User {
    private String email;

    public User(String username) {
        this.email = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
