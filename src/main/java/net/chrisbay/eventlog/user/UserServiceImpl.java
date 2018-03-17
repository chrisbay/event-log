package net.chrisbay.eventlog.user;

import net.chrisbay.eventlog.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by Chris Bay
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User save(UserDto userDto) throws EmailExistsException {

        User existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser != null)
            throw new EmailExistsException("The email address "
                    + userDto.getEmail() + " already exists in the system");

        User newUser = new User(
                userDto.getFullName(),
                userDto.getEmail(),
                passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(newUser);

        return newUser;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
