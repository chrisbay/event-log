package net.chrisbay.eventlog.integration;

import net.chrisbay.eventlog.data.UserRepository;
import net.chrisbay.eventlog.integration.config.IntegrationTestConfig;
import net.chrisbay.eventlog.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Chris Bay
 */
@RunWith(SpringRunner.class)
@IntegrationTestConfig
@ContextConfiguration
public class AuthenticationControllerTests {

    @Autowired private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    private static final String testUserFullName = "New User";
    private static final String testUserEmail = "test@launchcode.org";
    private static final String testUserPassword = "learntocode";

    @Before
    public void setUpUser() throws Exception {
        User user = new User(testUserFullName, testUserEmail, testUserPassword);
        userRepository.save(user);
    }

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
        User user = userRepository.findByEmail(email);
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
        mockMvc.perform(post("/login").with(csrf())
                .param("password", testUserPassword)
                .param("email", testUserEmail))
                .andDo(print());
//                .andExpect(status().is3xxRedirection())
//                .andExpect(header().string("Location", "/welcome"));
    }

}
