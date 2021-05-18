package application.remindermeeting.models;

import application.remindermeeting.entities.Employees;
import java.util.List;

public class ScheduleMeetingsRequest {

    private String title;
    private String dateMeetings;
    private List<EmployeeForScheduleRequest> employeesList;
    private String urlDocument;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateMeetings() {
        return dateMeetings;
    }

    public void setDateMeetings(String dateMeetings) {
        this.dateMeetings = dateMeetings;
    }

    public List<EmployeeForScheduleRequest> getEmployeesList() {
        return employeesList;
    }

    public void setEmployeesList(List<EmployeeForScheduleRequest> employeesList) {
        this.employeesList = employeesList;
    }

    public String getUrlDocument() {
        return urlDocument;
    }

    public void setUrlDocument(String urlDocument) {
        this.urlDocument = urlDocument;
    }
}
