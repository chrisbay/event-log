package net.chrisbay.eventlog.controllers;

import net.chrisbay.eventlog.models.Event;
import net.chrisbay.eventlog.models.Volunteer;
import net.chrisbay.eventlog.repositories.EventRepository;
import net.chrisbay.eventlog.repositories.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Chris Bay
 */
@Controller
@RequestMapping(value = "/events")
public class EventController extends AbstractBaseController {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    VolunteerRepository volunteerRepository;

    @GetMapping
    public String listEvents(Model model) {
        List<Event> allEvents = eventRepository.findAll();
        model.addAttribute("events", allEvents);
        return "events/list";
    }

    @GetMapping(value = "create")
    public String displayCreateEventForm(Model model, HttpServletRequest request) {
        model.addAttribute(new Event());
        model.addAttribute("actionUrl", request.getRequestURI());
        model.addAttribute("title", "Create Event");
        model.addAttribute("volunteers", volunteerRepository.findAll());
        return "events/create-or-update";
    }

    @PostMapping(value = "create")
    public String processCreateEventForm(@Valid @ModelAttribute Event event,
                                         Errors errors,
                                         @RequestParam("volunteers") List<Integer> volunteerUids) {

        if (errors.hasErrors())
            return "events/create-or-update";

        syncVolunteerLists(volunteerUids, event.getVolunteers());
        eventRepository.save(event);

        return "redirect:/events/detail/" + event.getUid();
    }

    @GetMapping(value = "detail/{uid}")
    public String displayEventDetails(@PathVariable int uid, Model model) {

        model.addAttribute("title", "Event Details");

        Optional<Event> result = eventRepository.findById(uid);
        if (result.isPresent()) {
            Event event = result.get();
            model.addAttribute(event);
            model.addAttribute("volunteerNames", event.getVolunteersFormatted());
        } else {
            model.addAttribute(MESSAGE_KEY, "warning|No event found with id: " + Integer.toString(uid));
        }

        return "events/details";
    }

    @GetMapping(value = "update/{uid}")
    public String displayUpdateEventForm(@PathVariable int uid, Model model, HttpServletRequest request) {

        model.addAttribute("title", "Update Event");
        model.addAttribute("actionUrl", request.getRequestURI());

        Optional<Event> event = eventRepository.findById(uid);
        if (event.isPresent()) {
            model.addAttribute(event.get());
            model.addAttribute("volunteers", volunteerRepository.findAll());
        } else {
            model.addAttribute(MESSAGE_KEY, "warning|No event found with id: " + Integer.toString(uid));
        }

        return "events/create-or-update";
    }

    @PostMapping(value = "update/{uid}")
    public String processUpdateEventForm(@Valid @ModelAttribute Event event,
                                         RedirectAttributes model,
                                         Errors errors,
                                         @RequestParam("volunteers") List<Integer> volunteerUids) {

        if (errors.hasErrors())
            return "events/create-or-update";

        syncVolunteerLists(volunteerUids, event.getVolunteers());
        eventRepository.save(event);
        model.addFlashAttribute(MESSAGE_KEY, "success|Updated event: " + event.getTitle());

        return "redirect:/events/detail/" + event.getUid();
    }

    @PostMapping(value = "delete/{uid}")
    public String processDeleteEventForm(@PathVariable int uid, RedirectAttributes model) {

        Optional<Event> result = eventRepository.findById(uid);
        if (result.isPresent()) {
            eventRepository.delete(result.get());
            model.addFlashAttribute(MESSAGE_KEY, "success|Event deleted");
            return "redirect:/events";
        } else {
            model.addFlashAttribute(MESSAGE_KEY, "danger|Event with ID does not exist: " +  uid);
            return "redirect:/events";
        }
    }

    private void syncVolunteerLists(List<Integer> volunteerUids, List<Volunteer> volunteers) {
        List<Volunteer> newVolunteerList = volunteerRepository.findAllById(volunteerUids);
        volunteers.removeIf(v -> volunteerUids.contains(v.getUid()));
        volunteers.addAll(newVolunteerList);
    }

}
