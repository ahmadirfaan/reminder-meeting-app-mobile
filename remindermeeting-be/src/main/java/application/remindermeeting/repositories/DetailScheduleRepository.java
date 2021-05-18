package application.remindermeeting.repositories;

import application.remindermeeting.entities.DetailSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailScheduleRepository extends JpaRepository<DetailSchedule,Integer> {
}
