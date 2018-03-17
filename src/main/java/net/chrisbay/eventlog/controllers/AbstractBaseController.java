package net.chrisbay.eventlog.controllers;

import net.chrisbay.eventlog.models.User;
import net.chrisbay.eventlog.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

/**
 * Created by Chris Bay
 */
public abstract class AbstractBaseController {

    @Autowired
    UserService userService;

    @ModelAttribute("user")
    public User getLoggedInUser(Principal user) {
        if (user != null)
            return userService.findByEmail(user.getName());
        return null;
    }

}
