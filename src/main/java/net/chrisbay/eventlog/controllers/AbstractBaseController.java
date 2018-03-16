package net.chrisbay.eventlog.controllers;

import net.chrisbay.eventlog.data.UserRepository;
import net.chrisbay.eventlog.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

/**
 * Created by Chris Bay
 */
public class AbstractBaseController {

    @Autowired
    UserRepository userRepository;

    @ModelAttribute("user")
    public User getLoggedInUser(Principal user) {
        if (user != null)
            return userRepository.findByEmail(user.getName());
        return null;
    }

}
