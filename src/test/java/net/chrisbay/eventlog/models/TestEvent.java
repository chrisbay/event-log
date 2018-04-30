package net.chrisbay.eventlog.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Chris Bay
 */
public class TestEvent {


    @Test(expected = IllegalArgumentException.class)
    public void testCreateEventCatchesNullTitle() {
        new Event(null, "", new Date(), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEventCatchesEmptyTitle() {
        new Event("", "", new Date(), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEventCatchesNullDescription() {
        new Event("Title", null, new Date(), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEventCatchesBlankDescription() {
        new Event("Title", "", new Date(), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEventCatchesNullDate() {
        new Event("Title", "", null, "");
    }

    @Test
    public void testCanAddVolunteers() {
        Event event = new Event("Title", "A great description", new Date(), "Place");
        Volunteer vol = new Volunteer("First", "Last");
        assertEquals(0, event.getVolunteers().size());
        event.addVolunteer(vol);
        assertEquals(1, event.getVolunteers().size());
    }

    @Test
    public void canCreateEventWithVolunteers() {
        Volunteer vol1 = new Volunteer("First", "Last");
        Volunteer vol2 = new Volunteer("First", "Last");
        List<Volunteer> vols = new ArrayList<>();
        vols.add(vol1);
        vols.add(vol2);
        Event event = new Event("Title", "A great description", new Date(), "Place", vols);
        assertEquals(vols.size(), event.getVolunteers().size());
    }

}
