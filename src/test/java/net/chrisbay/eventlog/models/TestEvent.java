package net.chrisbay.eventlog.models;

import org.junit.Test;

import java.util.Date;

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

}
