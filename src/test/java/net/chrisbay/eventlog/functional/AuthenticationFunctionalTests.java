package net.chrisbay.eventlog.functional;

import net.chrisbay.eventlog.functional.config.FunctionalTestConfig;
import net.chrisbay.eventlog.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Chris Bay
 */
@RunWith(SpringRunner.class)
@FunctionalTestConfig
@ContextConfiguration
public class AuthenticationFunctionalTests extends AbstractBaseFunctionalTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testCanViewRegistrationForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Register")))
                .andExpect(content().string(containsString("<form")));
    }

    @Test
    public void testCanRegister() throws Exception {
        String email = "newuser@domain.com";
        String password = "abc123";
        mockMvc.perform(post("/register").with(csrf())
                .param("fullName", "New User")
                .param("email", email)
                .param("password", password)
                .param("verifyPassword", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/welcome"));
        User user = userService.findByEmail(email);
        assertEquals(user.getEmail(), email);
    }

    @Test
    public void testRegistrationFormChecksPasswords() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                .param("fullName", "New User")
                .param("email", "newuser@domain.com")
                .param("password", "password")
                .param("verifyPassword", "passord"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Passwords do not match")));
    }

    @Test
    public void testRegistrationFormChecksEmailFormat() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                .param("fullName", "New User")
                .param("email", "newuser")
                .param("password", "password")
                .param("verifyPassword", "password"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Invalid email address")));
    }

    @Test
    public void testRegistrationFormChecksForExistingEmail() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                .param("fullName", TEST_USER_FULL_NAME)
                .param("email", TEST_USER_EMAIL)
                .param("password", TEST_USER_PASSWORD)
                .param("verifyPassword", TEST_USER_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TEST_USER_EMAIL + " already exists")));
    }

    @Test
    public void testRegistrationFormValidatesFullName() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                .param("fullName", "a")
                .param("email", "newuser")
                .param("password", "password")
                .param("verifyPassword", "password"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Full name must contain at least two characters")));
    }

    @Test
    public void testCanViewLoginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Log In")))
                .andExpect(content().string(containsString("<form")));
    }

    @Test
    public void testCanLogIn() throws Exception {
        mockMvc.perform(formLogin("/login")
                .user("email", TEST_USER_EMAIL)
                .password(TEST_USER_PASSWORD))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/welcome"));
    }

    @Test
    public void testRedirectsToWelcomeIfAlreadyLoggedIn() throws Exception {
        mockMvc.perform(get("/login")
                .with(user(TEST_USER_EMAIL)))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/welcome"));
    }

    @Test
    public void testViewWelcomeMessageAfterLogIn() throws Exception {
        mockMvc.perform(get("/welcome")
                .with(user(TEST_USER_EMAIL)))
                .andExpect(content().string(containsString(TEST_USER_FULL_NAME)));
    }

    @Test
    public void testCanLogOut() throws Exception {
        mockMvc.perform(formLogin("/login")
                .user("email", TEST_USER_EMAIL)
                .password(TEST_USER_PASSWORD));
        mockMvc.perform(post("/logout").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/login?logout"));
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "http://localhost/login"));
    }

}