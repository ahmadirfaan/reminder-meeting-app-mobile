package application.remindermeeting.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "schedulers")
@Entity
public class ScheduleMeetings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @Column(name = "date_meetings")
    private LocalDateTime dateMeetings;

    @OneToMany(targetEntity = DetailSchedule.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private List<DetailSchedule> detailScheduleList;

    @Column(name = "url_document")
    private String urlDocument;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDateMeetings() {
        return dateMeetings;
    }

    public void setDateMeetings(LocalDateTime dateMeetings) {
        this.dateMeetings = dateMeetings;
    }

    public List<DetailSchedule> getDetailScheduleList() {
        return detailScheduleList;
    }

    public void setDetailScheduleList(List<DetailSchedule> detailScheduleList) {
        this.detailScheduleList = detailScheduleList;
    }

    public String getUrlDocument() {
        return urlDocument;
    }

    public void setUrlDocument(String urlDocument) {
        this.urlDocument = urlDocument;
    }

    @Override
    public String toString() {
        return "ScheduleMeetings{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", dateMeetings=" + dateMeetings +
                ", detailScheduleList=" + detailScheduleList +
                ", urlDocument='" + urlDocument + '\'' +
                '}';
    }
}
