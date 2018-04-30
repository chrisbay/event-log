package net.chrisbay.eventlog.functional;

import net.chrisbay.eventlog.functional.config.FunctionalTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class VolunteerFunctionalTests extends AbstractEventBaseFunctionalTest {

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
                .andExpect(redirectedUrl("volunteers"));
        mockMvc.perform(get("/volunteers")
                .with(user(TEST_USER_EMAIL)))
                .andExpect(content().string(containsString("First Last")));
    }
}
