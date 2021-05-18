package application.remindermeeting.controllers;

import application.remindermeeting.entities.ScheduleMeetings;
import application.remindermeeting.models.PageSearch;
import application.remindermeeting.models.PagedList;
import application.remindermeeting.models.ResponseMessage;
import application.remindermeeting.models.ScheduleMeetingsRequest;
import application.remindermeeting.services.ScheduleMeetingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/schedulers")
public class ScheduleMeetingController {

    @Autowired
    private ScheduleMeetingsService scheduleMeetingService;


    @PostMapping
    public ResponseMessage<ScheduleMeetings> registerScheduleMeetings(@RequestBody @Valid ScheduleMeetingsRequest request) throws IOException {
        ScheduleMeetings entity = scheduleMeetingService.registerMeeting(request);
        return ResponseMessage.success(entity);
    }

    @PutMapping("/{id}")
    public ResponseMessage<ScheduleMeetings> editScheduleMeetingsById(@PathVariable Integer id, @RequestBody @Valid ScheduleMeetingsRequest model) {
        ScheduleMeetings entity = scheduleMeetingService.editMeeting(id, model);
        return ResponseMessage.success(entity);
    }

    @GetMapping("/{id}")
    public ResponseMessage<ScheduleMeetings> findEmployeeById(@PathVariable Integer id) {
        ScheduleMeetings entity = scheduleMeetingService.findById(id);
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        return ResponseMessage.success(entity);
    }

    @PutMapping("/delete/{id}")
    public ResponseMessage<String> softDeleteScheduleMeetingsById(@PathVariable Integer id) {
        ScheduleMeetings entity = scheduleMeetingService.findById(id);
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        entity = scheduleMeetingService.removeById(id);
        return ResponseMessage.success("Successfully deleted user data");
    }


    @GetMapping("/done/{idEmployee}")
    public ResponseMessage<List<ScheduleMeetings>> findAllScheduleMeetingsDone(@PathVariable Integer idEmployee) {
        System.out.println("Local Date Time Now + " + LocalDateTime.now());
        List<ScheduleMeetings> data = scheduleMeetingService.findAllDone(idEmployee);
        return ResponseMessage.success(data);
    }

    @GetMapping("/notdone/{idEmployee}")
    public ResponseMessage<List<ScheduleMeetings>> findAllScheduleMeetingsNotDone(@PathVariable Integer idEmployee) {
        System.out.println("Local Date Time Now + " + LocalDateTime.now());
        List<ScheduleMeetings> data = scheduleMeetingService.findAllNotDone(idEmployee);
        return ResponseMessage.success(data);
    }
}
