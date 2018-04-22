package net.chrisbay.eventlog.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Chris Bay
 */
@Entity
public class Event extends AbstractEntity {

    private static final String START_DATE_FORMAT_PATTERN = "MM/dd/yyyy";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT
            = new SimpleDateFormat(START_DATE_FORMAT_PATTERN);

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Please enter a valid date")
    @DateTimeFormat(pattern = START_DATE_FORMAT_PATTERN)
    private Date startDate;

    private String location;

    public Event() {}

    public Event(@NotBlank String title,
                 @NotNull String description,
                 @NotNull Date startDate,
                 String location) {

        if (title == null || title.length() == 0)
            throw new IllegalArgumentException("Title may not be blank");

        if (description == null || description.length() == 0)
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

    public String getFormattedStartDate() {
        return Event.SIMPLE_DATE_FORMAT.format(startDate);
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
