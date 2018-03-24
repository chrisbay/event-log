package net.chrisbay.eventlog.user;

import net.chrisbay.eventlog.forms.UserForm;
import net.chrisbay.eventlog.models.User;

/**
 * Created by Chris Bay
 */
public interface UserService {

    public User save(UserForm userForm) throws EmailExistsException;
    public User findByEmail(String email);

}
