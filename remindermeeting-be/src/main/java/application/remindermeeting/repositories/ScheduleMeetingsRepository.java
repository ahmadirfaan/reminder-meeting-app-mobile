package application.remindermeeting.repositories;

import application.remindermeeting.entities.ScheduleMeetings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleMeetingsRepository extends JpaRepository<ScheduleMeetings, Integer> {
}
