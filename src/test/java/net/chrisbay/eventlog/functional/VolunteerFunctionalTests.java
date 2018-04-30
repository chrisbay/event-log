package net.chrisbay.eventlog.functional;

import net.chrisbay.eventlog.functional.config.FunctionalTestConfig;
import net.chrisbay.eventlog.models.Event;
import net.chrisbay.eventlog.models.Volunteer;
import net.chrisbay.eventlog.repositories.VolunteerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Chris Bay
 */
@RunWith(SpringRunner.class)
@FunctionalTestConfig
@ContextConfiguration
public class VolunteerFunctionalTests extends AbstractEventBaseFunctionalTest {

    @Autowired
    VolunteerRepository volunteerRepository;

    private List<Volunteer> createAndSaveMultipleVolunteers(int numberOfVolunteers) {
        List<Volunteer> volunteers = new ArrayList<>();
        for (int i=0; i<numberOfVolunteers; i++) {
            Volunteer v = new Volunteer("First", "Last "+Integer.toString(i));
            volunteerRepository.save(v);
            volunteers.add(v);
        }
        return volunteers;
    }

    @Test
    public void testCanViewNewVolunteerForm() throws Exception {
        mockMvc.perform(get("/volunteers/create")
                .with(user(TEST_USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Create Volunteer")))
                .andExpect(xpath("//form[@id='volunteerForm']/@method").string("post"))
                .andExpect(xpath("//form[@id='volunteerForm']/@action").string("/volunteers/create"))
                .andExpect(xpath("//form//input[@name='firstName']").exists())
                .andExpect(xpath("//form//input[@name='lastName']").exists());
    }

    @Test
    public void testCanCreateVolunteer() throws Exception {
        mockMvc.perform(post("/volunteers/create")
                .with(user(TEST_USER_EMAIL))
                .with(csrf())
                .param("firstName", "First")
                .param("lastName", "Last"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/volunteers"));
        mockMvc.perform(get("/volunteers")
                .with(user(TEST_USER_EMAIL)))
                .andExpect(content().string(containsString("First Last")));
    }

    @Test
    public void testCanViewAddVolunteerButtonOnVolunteerListing() throws Exception {
        mockMvc.perform(get("/volunteers")
                .with(user(TEST_USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(xpath("//a[@href='/volunteers/create']").string(containsString("Create Volunteer")));
    }

    @Test
    public void testCanViewVolunteersWhenCreatingEvent() throws Exception {
        int numberOfVolunteers = 5;
        List<Volunteer> vols = createAndSaveMultipleVolunteers(numberOfVolunteers);
        mockMvc.perform(get("/events/create")
                .with(csrf())
                .with(user(TEST_USER_EMAIL)))
                .andExpect(xpath("//select[@name='volunteers']/option").nodeCount(numberOfVolunteers));
    }

    @Test
    public void testCanAddVolunteersWhenCreatingEvent() throws Exception {
        int numberOfVolunteers = 2;
        List<Volunteer> vols = createAndSaveMultipleVolunteers(numberOfVolunteers);
        mockMvc.perform(post("/events/create")
                .with(user(TEST_USER_EMAIL))
                .with(csrf())
                .param("title", "Test Event")
                .param("description", "This event will be great!")
                .param("startDate", "11/11/11")
                .param("location", "")
                .param("volunteers", Integer.toString(vols.get(0).getUid()))
                .param("volunteers", Integer.toString(vols.get(1).getUid())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/events/detail/*"));
        Event event = eventRepository.findAll().get(0);
        assertEquals(event.getVolunteers().size(), numberOfVolunteers);
    }

    @Test
    public void testCanUpdateVolunteersForEvent() throws Exception {
        int numberOfVolunteers = 3;
        List<Volunteer> vols = createAndSaveMultipleVolunteers(numberOfVolunteers);
        mockMvc.perform(post("/events/create")
                .with(user(TEST_USER_EMAIL))
                .with(csrf())
                .param("title", "Test Event")
                .param("description", "This event will be great!")
                .param("startDate", "11/11/11")
                .param("location", "")
                .param("volunteers", Integer.toString(vols.get(0).getUid()))
                .param("volunteers", Integer.toString(vols.get(1).getUid())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/events/detail/*"));
        Event event = eventRepository.findAll().get(0);
        mockMvc.perform(post("/events/update/{uid}", event.getUid())
                .with(csrf())
                .with(user(TEST_USER_EMAIL))
                .param("uid", Integer.toString(event.getUid()))
                .param("title", event.getTitle())
                .param("description", event.getDescription())
                .param("startDate", event.getFormattedStartDate())
                .param("location", event.getLocation())
                .param("volunteers", Integer.toString(vols.get(1).getUid()))
                .param("volunteers", Integer.toString(vols.get(2).getUid())))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/events/detail/"+event.getUid()));
        Optional<Event> updatedEventRes = eventRepository.findById(event.getUid());
        assertTrue(updatedEventRes.isPresent());
        Event updatedEvent = updatedEventRes.get();
        assertEquals(updatedEvent.getVolunteers().size(), 2);
    }
}
