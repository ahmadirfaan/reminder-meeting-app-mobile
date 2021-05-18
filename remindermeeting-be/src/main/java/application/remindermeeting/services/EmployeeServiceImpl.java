package application.remindermeeting.services;

import application.remindermeeting.entities.Employees;
import application.remindermeeting.models.LoginModels;
import application.remindermeeting.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class EmployeeServiceImpl extends AbstractService<Employees, Integer>
        implements EmployeeService{

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository repository) {
        super(repository);
    }


    @Override
    public Employees editEmployee(Integer id,Employees request) {
        Employees entity = findById(id);
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        entity.setEmail(request.getEmail());
        entity.setPassword(request.getPassword());
        entity.setName(request.getName());
        entity = save(entity);
        return entity;
    }

    @Override
    public Employees loginEmployee(LoginModels loginModels) {
        List<Employees> employeesList = findAll();
        return employeesList.stream().filter(
                employees -> employees.getEmail().equals(loginModels.getEmail()) && employees.getPassword().equals(loginModels.getPassword())
        ).findAny().orElse(null);
    }

}
