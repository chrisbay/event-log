package net.chrisbay.eventlog.functional;

import net.chrisbay.eventlog.models.Event;
import net.chrisbay.eventlog.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Chris Bay
 */
public abstract class AbstractEventBaseFunctionalTest extends AbstractBaseFunctionalTest {

    @Autowired
    EventRepository eventRepository;

    protected Event createAndSaveSingleEvent() {
        Date eventDate = new Date();
        Event newEvent = new Event("The Title", "The Description", eventDate, "The Location");
        eventRepository.save(newEvent);
        return newEvent;
    }

    protected List<Event> createAndSaveMultipleEvents(int numberOfEvents) {
        List<Event> events = new ArrayList<>();
        for (int i=0; i<numberOfEvents; i++) {
            Event e = new Event("Event "+Integer.toString(i), "The Description", new Date(), "");
            eventRepository.save(e);
            events.add(e);
        }
        return events;
    }

}
