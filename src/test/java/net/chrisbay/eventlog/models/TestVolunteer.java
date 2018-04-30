package net.chrisbay.eventlog.models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void testGetFullName() {
        Volunteer vol = new Volunteer("First", "Last");
        assertEquals("First Last", vol.getFullName());
    }

}
