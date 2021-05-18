package application.remindermeeting.models;

public class EmployeeForScheduleRequest {
    /*Merupakan request untuk array pada sebuah request schedules sehingga Json hanya
    menerima sebuah Id saja daripada harus membuat sebuah POJO Class
     */

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
