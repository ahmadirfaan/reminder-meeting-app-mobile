package application.remindermeeting.controllers;

import application.remindermeeting.entities.Employees;
import application.remindermeeting.models.LoginModels;
import application.remindermeeting.models.ResponseMessage;
import application.remindermeeting.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeServices;


    @PostMapping
    public ResponseMessage<Employees> registerEmployees(@RequestBody @Valid Employees request) throws IOException {
        Employees entity = employeeServices.save(request);
        return ResponseMessage.success(entity);
    }

    @PostMapping("/login")
    public ResponseMessage<Employees> loginEmployees(@RequestBody @Valid LoginModels loginModels) throws IOException {
        Employees login = employeeServices.loginEmployee(loginModels);
        System.out.println("Login ini adalah " + login);
        if(login == null) {
            return new ResponseMessage<>(200,"Email or Password is Wrong",null);
        } else {
            return new ResponseMessage<>(200,"Success Login",login);
        }
    }

    @PutMapping("/{id}")
    public ResponseMessage<Employees> editEmployeesById(@PathVariable Integer id, @RequestBody @Valid Employees model) {
        Employees entity = employeeServices.editEmployee(id, model);
        return ResponseMessage.success(entity);
    }

    @GetMapping("/{id}")
    public ResponseMessage<Employees> findEmployeeById(@PathVariable Integer id) {
        Employees entity = employeeServices.findById(id);
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        return ResponseMessage.success(entity);
    }

    @PutMapping("/delete/{id}")
    public ResponseMessage<String> softDeleteEmployeesById(@PathVariable Integer id) {
        Employees entity = employeeServices.findById(id);
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        entity = employeeServices.removeById(id);
        return ResponseMessage.success("Successfully deleted user data");
    }


    @GetMapping()
    public ResponseMessage<List<Employees>> findAllEmployeesPagination() {
        List<Employees> data = employeeServices.findAll();
        return ResponseMessage.success(data);
    }
}
