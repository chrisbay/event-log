package net.chrisbay.eventlog.models;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * Created by Chris Bay
 */
@Entity
public class User extends AbstractEntity {

    @NotNull
    private String email;

    @NotNull
    private String password;
    private Boolean enabled = true;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = encoder.encode(password);
    }

    public String getEmail() {
        return email;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
//        this.enabled = enabled;
    }

}
