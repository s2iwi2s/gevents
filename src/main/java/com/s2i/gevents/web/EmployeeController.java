package com.s2i.gevents.web;

import com.s2i.gevents.domain.Employee;
import com.s2i.gevents.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    Logger log = LoggerFactory.getLogger(this.getClass());

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<?> getAllEmployees() throws Exception {
        List<Employee> list = employeeService.getAllEmployees();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/employee/age/greaterThan/{age}")
    public ResponseEntity<?> getAllEmployeesWithAgeGreaterThan(@PathVariable Integer age) throws Exception {
        List<Employee> list = employeeService.getAllEmployeesWithAgeGreaterThan(age);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/employee/age/lessThan/{age}")
    public ResponseEntity<?> getAllEmployeesWithAgeLessThan(@PathVariable Integer age) throws Exception {
        List<Employee> list = employeeService.getAllEmployeesWithAgeLessThan(age);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/employee/age/equal/{age}")
    public ResponseEntity<?> getAllEmployeesWithAgeEqual(@PathVariable Integer age) throws Exception {
        List<Employee> list = employeeService.getAllEmployeesWithAgeEqual(age);
        return ResponseEntity.ok(list);
    }
}