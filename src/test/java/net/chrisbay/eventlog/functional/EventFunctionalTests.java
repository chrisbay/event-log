package net.chrisbay.eventlog.functional;

import net.chrisbay.eventlog.functional.config.FunctionalTestConfig;
import net.chrisbay.eventlog.models.Event;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
public class EventFunctionalTests extends AbstractEventBaseFunctionalTest {

    @Test
    public void testIndexShowsRecentEvents() throws Exception {
        Event event = createAndSaveSingleEvent();
        mockMvc.perform(get("/")
                .with(user(TEST_USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(event.getTitle())))
                .andExpect(content().string(containsString(event.getFormattedStartDate())));
    }

    @Test
    public void testCanViewNewEventForm() throws Exception {
        mockMvc.perform(get("/events/create")
                .with(user(TEST_USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Create Event")))
                .andExpect(xpath("//form[@id='eventForm']/@method").string("post"))
                .andExpect(xpath("//form[@id='eventForm']/@action").string("/events/create"))
                .andExpect(xpath("//form//input[@name='title']").exists())
                .andExpect(xpath("//form//input[@name='startDate']").exists())
                .andExpect(xpath("//form//textarea[@name='description']").exists())
                .andExpect(xpath("//form//input[@name='location']").exists());
    }

    @Test
    public void testCanCreateEvent() throws Exception {
        mockMvc.perform(post("/events/create")
                .with(user(TEST_USER_EMAIL))
                .with(csrf())
                .param("title", "Test Event")
                .param("description", "This event will be great!")
                .param("startDate", "11/11/11")
                .param("location", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/events/detail/*"));
    }

    @Test
    public void testCanViewEventDetails() throws Exception {
        Event event = createAndSaveSingleEvent();
        List<String> eventFields = Arrays.asList(
                event.getTitle(),
                event.getFormattedStartDate(),
                event.getLocation(),
                event.getDescription()
        );
        mockMvc.perform(get("/events/detail/{uid}", event.getUid())
                .with(user(TEST_USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(content().string(stringContainsInOrder(eventFields)));
    }

    @Test
    public void testDisplayErrorMessageOnInvalidEventId() throws Exception {
        mockMvc.perform(get("/events/detail/{uid}", -1)
                .with(csrf())
                .with(user(TEST_USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("No event found with id: -1")));
    }

    @Test
    public void testCreateEventValidatesTitle() throws Exception {
        mockMvc.perform(post("/events/create")
                .with(csrf())
                .with(user(TEST_USER_EMAIL))
                .param("title", "")
                .param("description", "Test description")
                .param("startDate", "11/11/11"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("event", "title"));
    }

    @Test
    public void testCreateEventValidatesDescription() throws Exception {
        mockMvc.perform(post("/events/create")
                .with(csrf())
                .with(user(TEST_USER_EMAIL))
                .param("title", "Test title")
                .param("description", "")
                .param("startDate", "11/11/11"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("event", "description"));
    }

    @Test
    public void testCreateEventValidatesStartDate() throws Exception {
        mockMvc.perform(post("/events/create")
                .with(csrf())
                .with(user(TEST_USER_EMAIL))
                .param("title", "Test Title")
                .param("description", "Test description")
                .param("startDate", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("event", "startDate"));
    }

    @Test
    public void testEventDetailsPageDisplaysEditButton() throws Exception {
        Event event = createAndSaveSingleEvent();
        mockMvc.perform(get("/events/detail/{uid}", event.getUid())
                .with(user(TEST_USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(xpath("//a[contains(@class,'btn') and @href='/events/update/%s']", event.getUid())
                        .string(Matchers.containsString("Edit")));
    }

    @Test
    public void testCanViewUpdateEventForm() throws Exception {
        Event event = createAndSaveSingleEvent();
        mockMvc.perform(get("/events/update/{uid}", event.getUid())
                .with(user(TEST_USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Update Event")))
                .andExpect(xpath("//form[@method='post' and @action='/events/update/%s']",
                        event.getUid()).exists())
                .andExpect(xpath("//form//input[@name='uid' and @value='%s']",
                        event.getUid()).exists())
                .andExpect(xpath("//form//input[@name='title' and @value='%s']",
                        event.getTitle()).exists())
                .andExpect(xpath("//form//input[@name='startDate' and @value='%s']",
                        event.getFormattedStartDate()).exists())
                .andExpect(xpath("//form//input[@name='location' and @value='%s']",
                        event.getLocation()).exists())
                .andExpect(xpath("//form//textarea[@name='description']")
                        .string(event.getDescription()));
    }

    @Test
    public void testCanUpdateEvent() throws Exception {
        Event event = createAndSaveSingleEvent();
        String newTitle = event.getTitle() + "UPDATED";
        mockMvc.perform(post("/events/update/{uid}", event.getUid())
                .with(csrf())
                .with(user(TEST_USER_EMAIL))
                .param("uid", Integer.toString(event.getUid()))
                .param("title", newTitle)
                .param("description", event.getDescription())
                .param("startDate", event.getFormattedStartDate())
                .param("location", event.getLocation()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/events/detail/"+event.getUid()));
        Optional<Event> updatedEventRes = eventRepository.findById(event.getUid());
        assertTrue(updatedEventRes.isPresent());
        Event updatedEvent = updatedEventRes.get();
        assertEquals(newTitle, updatedEvent.getTitle());
    }

    @Test
    public void testEventDetailPageContainsDeleteButton() throws Exception {
        Event event = createAndSaveSingleEvent();
        mockMvc.perform(get("/events/detail/{uid}", event.getUid())
                .with(user(TEST_USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(xpath("//form[@method='post' and @action='/events/delete/%s']", event.getUid())
                    .exists())
                .andExpect(xpath("//form//button")
                        .string(Matchers.containsString("Delete")));
    }

    @Test
    public void testCanDeleteEvent() throws Exception {
        Event event = createAndSaveSingleEvent();
        mockMvc.perform(post("/events/delete/{uid}", event.getUid())
                .with(user(TEST_USER_EMAIL))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/events"));
        Optional<Event> updatedEventRes = eventRepository.findById(event.getUid());
        assertFalse(updatedEventRes.isPresent());
    }

}
