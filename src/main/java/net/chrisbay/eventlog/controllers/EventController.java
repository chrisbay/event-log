package net.chrisbay.eventlog.controllers;

import net.chrisbay.eventlog.models.Event;
import net.chrisbay.eventlog.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by Chris Bay
 */
@Controller
@RequestMapping(value = "/events")
public class EventController extends AbstractBaseController {

    @Autowired
    EventRepository eventRepository;

    @GetMapping
    public String listEvents(Model model) {
        List<Event> allEvents = eventRepository.findAll();
        model.addAttribute("events", allEvents);
        return "events/list";
    }

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

        return "redirect:/events/" + event.getUid();
    }

    @GetMapping(value = "{uid}")
    public String displayEventDetails(@PathVariable int uid, Model model) {

        model.addAttribute("title", "Event Details");

        Optional<Event> event = eventRepository.findById(uid);
        if (event.isPresent()) {
            model.addAttribute(event);
        } else {
            model.addAttribute(MESSAGE_KEY, "danger|No event found with id: " + Integer.toString(uid));
        }

        return "events/details";

    }

}
