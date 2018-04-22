package net.chrisbay.eventlog.functional;

import net.chrisbay.eventlog.functional.config.FunctionalTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

/**
 * Created by Chris Bay
 */
@RunWith(SpringRunner.class)
@FunctionalTestConfig
@ContextConfiguration
public class NavigationFunctionalTests extends AbstractBaseFunctionalTest {

    private void navbarContainsNavItem(String text, String url) throws Exception {
        mockMvc.perform(get("/")
                .with(user(TEST_USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(xpath("//nav[contains(@class,'navbar')]//a[contains(@class,'nav-link') and @href='%s']", url)
                        .string(text));
    }

    @Test
    public void testNavbarLinksToMainEventListing() throws Exception {
        navbarContainsNavItem("Events", "/");
    }

    @Test
    public void testNavbarLinksToCreateForm() throws Exception {
        navbarContainsNavItem("Create", "/events/create");
    }
}
