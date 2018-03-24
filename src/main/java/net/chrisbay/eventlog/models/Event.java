package net.chrisbay.eventlog.models;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Chris Bay
 */
@Entity
public class Event extends AbstractEntity {

    @NotBlank
    private String title;

    @NotNull
    private String description;

    private int duration;

    @NotNull
    private Date date;

    private String location;

    public Event() {}

    public Event(@NotBlank String title, @NotNull String description, int duration, @NotNull Date date, String location) {

        if (title == null || title.length() == 0)
            throw new IllegalArgumentException("Title may not be blank");

        if (description == null)
            throw new IllegalArgumentException("Description may not be null");

        if (duration <= 0)
            throw new IllegalArgumentException("Duration must be greater than 0");

        if (date == null)
            throw new IllegalArgumentException("Date may not be null");

        this.title = title;
        this.description = description;
        this.duration = duration;
        this.date = date;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", date=" + date +
                '}';
    }
}
