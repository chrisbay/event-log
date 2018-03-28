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

    @NotNull
    private Date startDate;

    private String location;

    public Event() {}

    public Event(@NotBlank String title,
                 @NotNull String description,
                 @NotNull Date startDate,
                 String location) {

        if (title == null || title.length() == 0)
            throw new IllegalArgumentException("Title may not be blank");

        if (description == null)
            throw new IllegalArgumentException("Description may not be null");

        if (startDate == null)
            throw new IllegalArgumentException("Start date may not be null");

        this.title = title;
        this.description = description;
        this.startDate = startDate;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
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
                ", startDate=" + startDate +
                '}';
    }
}
