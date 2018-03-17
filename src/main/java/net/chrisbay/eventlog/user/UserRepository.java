package net.chrisbay.eventlog.user;

import net.chrisbay.eventlog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Chris Bay
 */
interface UserRepository extends JpaRepository<User, Integer> {

    public User findByEmail(String email);
}
