package net.chrisbay.eventlog.functional;

import net.chrisbay.eventlog.functional.config.FunctionalTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

/**
 * Created by Chris Bay
 */
@RunWith(SpringRunner.class)
@FunctionalTestConfig
@ContextConfiguration
public class EventListingFunctionalTests extends AbstractEventBaseFunctionalTest {

    @Test
    public void testEventTitlesLinkToEventDetailsPages() throws Exception {
        int numberOfEvents = 5;
        createAndSaveMultipleEvents(numberOfEvents);
        mockMvc.perform(get("/")
                .with(user(TEST_USER_EMAIL)))
                .andExpect(xpath("//tbody//tr//a[starts-with(@href,'/events/detail/')]")
                        .nodeCount(numberOfEvents));
    }

}
