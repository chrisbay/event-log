package net.chrisbay.eventlog.controllers;

import net.chrisbay.eventlog.models.Event;
import net.chrisbay.eventlog.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Created by Chris Bay
 */
@Controller
public class HomeController {

    @Autowired
    EventRepository eventRepository;

    @GetMapping(value = "/")
    public String index(Model model) {
        List<Event> allEvents = eventRepository.findAll();
        model.addAttribute("events", allEvents);
        return "events/list";
    }
}
