package net.chrisbay.eventlog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Chris Bay
 */
@Controller
public class HomeController extends AbstractBaseController {

    @RequestMapping(value = "/welcome")
    public String welcome(Model model) {
        return "index";
    }

}
