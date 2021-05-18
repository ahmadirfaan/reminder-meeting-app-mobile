package application.remindermeeting.services;

import application.remindermeeting.entities.ScheduleMeetings;
import application.remindermeeting.models.ScheduleMeetingsRequest;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ScheduleMeetingsService extends CommonService<ScheduleMeetings, Integer> {
    ScheduleMeetings editMeeting(Integer id, ScheduleMeetingsRequest request);
    ScheduleMeetings registerMeeting(ScheduleMeetingsRequest request);
    List<ScheduleMeetings> findAllDone(Integer idEmployee);
    List<ScheduleMeetings> findAllNotDone(Integer idEmployee);
}
