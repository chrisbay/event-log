package net.chrisbay.eventlog.repositories;

import net.chrisbay.eventlog.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Chris Bay
 */

public interface EventRepository extends JpaRepository<Event, Integer> {
}
