package application.remindermeeting.services;

import application.remindermeeting.entities.Employees;
import application.remindermeeting.models.LoginModels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface EmployeeService extends CommonService<Employees,Integer> {
    Employees editEmployee(Integer id,Employees request);
    Employees loginEmployee(LoginModels loginModels);
}
