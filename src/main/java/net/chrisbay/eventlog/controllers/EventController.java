package net.chrisbay.eventlog.controllers;

import net.chrisbay.eventlog.models.Event;
import net.chrisbay.eventlog.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Chris Bay
 */
@Controller
@RequestMapping(value = "/events")
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @GetMapping(value = "create")
    public String renderCreateEventForm(Model model) {
        model.addAttribute(new Event());
        model.addAttribute("title", "Create Event");
        return "events/create";
    }

    @PostMapping(value = "create")
    public String processCreateEventForm(@Valid @ModelAttribute Event event, Errors errors) {

        if (errors.hasErrors())
            return "events/create";

        eventRepository.save(event);

        return "redirect:" + event.getUid();
    }

    @GetMapping(value = "{id}")
    public String displayEventDetails(@PathVariable int id, Model model) {

        Event event = eventRepository.findById(id).get();

        model.addAttribute("title", "Event Details");
        model.addAttribute(event);
        return "events/details";
    }

}
