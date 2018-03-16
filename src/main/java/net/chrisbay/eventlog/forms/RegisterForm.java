package net.chrisbay.eventlog.forms;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by Chris Bay
 */
public class RegisterForm {

    @NotNull
    @Email(message = "Invalid email address")
    private String email;

    @NotNull
    @Pattern(regexp = "(\\S){6,20}", message = "Password must have 6-20 non-whitespace characters")
    private String password;

    @NotNull(message = "Passwords do not match")
    private String verifyPassword;

    public RegisterForm() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        checkPasswordForRegistration();
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
        checkPasswordForRegistration();
    }

    private void checkPasswordForRegistration() {
        if (!getPassword().equals(verifyPassword)) {
            verifyPassword = null;
        }
    }

}
