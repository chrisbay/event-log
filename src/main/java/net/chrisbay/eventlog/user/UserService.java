package net.chrisbay.eventlog.user;

import net.chrisbay.eventlog.models.User;

/**
 * Created by Chris Bay
 */
public interface UserService {

    public User save(UserDto userDto) throws EmailExistsException;
    public User findByEmail(String email);

}
