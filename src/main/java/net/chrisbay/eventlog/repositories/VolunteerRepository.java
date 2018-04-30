package net.chrisbay.eventlog.repositories;

import net.chrisbay.eventlog.models.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Chris Bay
 */
public interface VolunteerRepository extends JpaRepository<Volunteer, Integer> {
}
