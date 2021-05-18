package application.remindermeeting.entities;

import javax.persistence.*;

@Table(name = "detail_schedule")
@Entity
public class DetailSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private Employees employee;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employees getEmployee() {
        return employee;
    }

    public void setEmployee(Employees employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "DetailSchedule{" +
                "id=" + id +
                ", employee=" + employee +
                '}';
    }
}
