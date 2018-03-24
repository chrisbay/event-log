package net.chrisbay.eventlog.models;

import org.junit.Test;

import java.util.Date;

/**
 * Created by Chris Bay
 */
public class TestEvent {


    @Test(expected = IllegalArgumentException.class)
    public void testCreateEventCatchesNullTitle() {
        new Event(null, "", 1, new Date(), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEventCatchesEmptyTitle() {
        new Event("", "", 1, new Date(), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEventCatchesNullDescription() {
        new Event("Title", null, 1, new Date(), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEventCatchesNonpositiveDuration() {
        new Event("Title", "", 0, new Date(), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEventCatchesNullDate() {
        new Event("Title", "", 1, null, "");
    }

}
