package application.remindermeeting.services;

import application.remindermeeting.entities.DetailSchedule;
import application.remindermeeting.entities.Employees;
import application.remindermeeting.entities.ScheduleMeetings;
import application.remindermeeting.exceptions.EntityNotFoundException;
import application.remindermeeting.models.ScheduleMeetingsRequest;
import application.remindermeeting.repositories.EmployeeRepository;
import application.remindermeeting.repositories.ScheduleMeetingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleMeetingsServiceImpl extends AbstractService<ScheduleMeetings, Integer> implements ScheduleMeetingsService {

    private final DateTimeFormatter formatterBooking = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm")
            .toFormatter();

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    public ScheduleMeetingsServiceImpl(ScheduleMeetingsRepository repository) {
        super(repository);
    }

    @Override
    public ScheduleMeetings editMeeting(Integer id, ScheduleMeetingsRequest request) {
        List<DetailSchedule> detailScheduleList = new ArrayList<>();
        ScheduleMeetings entity = findById(id);
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        LocalDateTime dateMeeting = LocalDateTime.parse(request.getDateMeetings(), formatterBooking);
        for (int i = 0; i < request.getEmployeesList().size(); i++) {
            DetailSchedule detailSchedule = new DetailSchedule();
            Employees employees = employeeRepository.findById(request.getEmployeesList().get(i).getId()).orElse(null);
            if (employees == null) {
                throw new EntityNotFoundException();
            } else {
                detailSchedule.setEmployee(employees);
                detailScheduleList.add(detailSchedule);
            }
        }
        entity.setTitle(request.getTitle());
        entity.setDetailScheduleList(detailScheduleList);
        entity.setUrlDocument(request.getUrlDocument());
        entity.setDateMeetings(dateMeeting);
        entity = save(entity);
        return entity;
    }

    @Override
    public ScheduleMeetings registerMeeting(ScheduleMeetingsRequest request) {
        List<DetailSchedule> detailScheduleList = new ArrayList<>();
        LocalDateTime dateMeeting = LocalDateTime.parse(request.getDateMeetings(), formatterBooking);
        ScheduleMeetings saveScheduleMeeting = new ScheduleMeetings();
        saveScheduleMeeting.setTitle(request.getTitle());
        saveScheduleMeeting.setUrlDocument(request.getUrlDocument());
        for (int i = 0; i < request.getEmployeesList().size(); i++) {
            DetailSchedule detailSchedule = new DetailSchedule();
            Employees employees = employeeRepository.findById(request.getEmployeesList().get(i).getId()).orElse(null);
            if (employees == null) {
                throw new EntityNotFoundException();
            } else {
                detailSchedule.setEmployee(employees);
                detailScheduleList.add(detailSchedule);
            }
        }
        saveScheduleMeeting.setDetailScheduleList(detailScheduleList);
        saveScheduleMeeting.setDateMeetings(dateMeeting);
        saveScheduleMeeting = save(saveScheduleMeeting);
        return saveScheduleMeeting;
    }

    @Override
    public List<ScheduleMeetings> findAllDone(Integer idEmployee) {
        List<ScheduleMeetings> scheduleMeetingsList = findAll();
        Employees employee = employeeRepository.findById(idEmployee).orElse(null);
        if (employee == null) {
            throw new EntityNotFoundException();
        }
        List<ScheduleMeetings> data = scheduleMeetingsList.stream().filter(
                s -> s.getDateMeetings().isBefore(LocalDateTime.now()) && s.getDetailScheduleList().stream().anyMatch(d -> d.getEmployee().equals(employee))
        ).sorted(Comparator.comparing(ScheduleMeetings::getDateMeetings)).collect(Collectors.toList());
        Collections.reverse(data);
        return data;
    }

    @Override
    public List<ScheduleMeetings> findAllNotDone(Integer idEmployee) {
        List<ScheduleMeetings> scheduleMeetingsList = findAll();
        Employees employee = employeeRepository.findById(idEmployee).orElse(null);
        if (employee == null) {
            throw new EntityNotFoundException();
        }
        List<ScheduleMeetings> data = scheduleMeetingsList.stream().filter(
                s -> s.getDateMeetings().isAfter(LocalDateTime.now()) && s.getDetailScheduleList().stream().anyMatch(d -> d.getEmployee().equals(employee))
        ).sorted(Comparator.comparing(ScheduleMeetings::getDateMeetings)).collect(Collectors.toList());
        return data;
    }


}
