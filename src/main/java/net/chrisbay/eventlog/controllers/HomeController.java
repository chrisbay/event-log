package net.chrisbay.eventlog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Chris Bay
 */
@Controller
public class HomeController extends AbstractBaseController {

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

}
