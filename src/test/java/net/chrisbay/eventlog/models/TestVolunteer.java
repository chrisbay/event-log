package net.chrisbay.eventlog.models;

import org.junit.Test;

/**
 * Created by Chris Bay
 */
public class TestVolunteer {

    @Test(expected = IllegalArgumentException.class)
    public void testCreateVolunteerValidatesFirstName() {
        new Volunteer("ab", "abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateVolunteerValidatesLastName() {
        new Volunteer("abc", "ab");
    }

}
