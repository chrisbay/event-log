package net.chrisbay.eventlog.controllers;

import net.chrisbay.eventlog.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * Created by Chris Bay
 */
@Controller
public class HomeController extends AbstractBaseController {

    @RequestMapping(value = "/welcome")
    public String welcome(Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute(MESSAGE_KEY, "success|Welcome, "+user.getFullName());
        return "index";
    }

    @RequestMapping(value = "/")
    public String index() {
        return "redirect:/welcome";
    }

}
