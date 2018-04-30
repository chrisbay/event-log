package net.chrisbay.eventlog.models;

import javax.persistence.Entity;
import javax.validation.constraints.Size;

/**
 * Created by Chris Bay
 */
@Entity
public class Volunteer extends AbstractEntity {

    @Size(min = 3, message = "Volunteer names must be at least 3 characters long")
    private String firstName;

    @Size(min = 3, message = "Volunteer names must be at least 3 characters long")
    private String lastName;

    public Volunteer() {}

    public Volunteer(@Size(min = 3, message = "Volunteer names must be at least 3 characters long") String firstName,
                     @Size(min = 3, message = "Volunteer names must be at least 3 characters long") String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
