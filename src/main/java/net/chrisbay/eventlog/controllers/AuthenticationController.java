package net.chrisbay.eventlog.controllers;

import net.chrisbay.eventlog.forms.RegisterForm;
import net.chrisbay.eventlog.models.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * Created by Chris Bay
 */
@Controller
public class AuthenticationController extends AbstractBaseController {

    @GetMapping(value = "/register")
    public String registerForm(Model model) {
        model.addAttribute(new RegisterForm());
        model.addAttribute("title", "Register");
        return "register";
    }

    @PostMapping(value = "/register")
    public String register(@ModelAttribute @Valid RegisterForm form, Errors errors) {

        if (errors.hasErrors()) {
            return "register";
        }

        User existingUser = userRepository.findByEmail(form.getEmail());

        if (existingUser != null) {
            errors.rejectValue("username", "username.alreadyexists", "A user with that email already exists");
            return "register";
        }

        User newUser = new User(form.getFullName(), form.getEmail(), form.getPassword());
        userRepository.save(newUser);

        return "redirect:";
    }

    @GetMapping(value = "/login")
    public String login(Model model, String error, String logout) {

        if (error != null)
            model.addAttribute("errorMsg", "Your username and password are invalid.");

        if (logout != null)
            model.addAttribute("msg", "You have been logged out successfully.");

        // TODO - Check to see if user is already logged in

        return "login";
    }

}
