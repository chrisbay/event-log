package net.chrisbay.eventlog.functional;

import net.chrisbay.eventlog.functional.config.FunctionalTestConfig;
import net.chrisbay.eventlog.repositories.EventRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.containsString;
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
public class EventFunctionalTests extends AbstractBaseFunctionalTest {

    @Autowired
    EventRepository eventRepository;

    @Test
    public void testCanViewNewEventForm() throws Exception {
        mockMvc.perform(get("/events/create")
                .with(user(TEST_USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Create Event")))
                .andExpect(content().string(containsString("<form")))
                .andExpect(xpath("//input[@name='title']").exists())
                .andExpect(xpath("//input[@name='startDate']").exists())
                .andExpect(xpath("//textarea[@name='description']").exists())
                .andExpect(xpath("//input[@name='location']").exists());
    }

    @Test
    public void testCanCreateEvent() throws Exception {
        long numEvents = eventRepository.count();
        mockMvc.perform(post("/events/create").with(csrf())
                .param("title", "Test Event")
                .param("description", "This event will be great!")
                .param("date", "")
                .param("location", ""))
                .andExpect(status().is3xxRedirection());
    }

}
