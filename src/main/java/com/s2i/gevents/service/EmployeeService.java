package com.s2i.gevents.service;

import com.s2i.gevents.domain.Employee;
import com.s2i.gevents.service.dto.EmployeeResponseDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final RestTemplate restTemplate;
    private final String serviceUrl;

    public EmployeeService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(60000))
                .setReadTimeout(Duration.ofMillis(60000))
                .build();
        this.serviceUrl = "https://dummy.restapiexample.com/api/v1";
    }

    public List<Employee> getAllEmployees() throws Exception {
        ResponseEntity<EmployeeResponseDTO> responseEntity = this.restTemplate.getForEntity(serviceUrl + "/employees", EmployeeResponseDTO.class);
        return Objects.requireNonNull(responseEntity.getBody()).getData();
    }

    public List<Employee> getAllEmployeesWithAgeGreaterThan(Integer age) throws Exception {
        return this.getAllEmployees().stream().filter(employee -> employee.getEmployeeAge() > age).collect(Collectors.toList());
    }

    public List<Employee> getAllEmployeesWithAgeLessThan(Integer age) throws Exception {
        return this.getAllEmployees().stream().filter(employee -> employee.getEmployeeAge() < age).collect(Collectors.toList());
    }

    public List<Employee> getAllEmployeesWithAgeEqual(Integer age) throws Exception {
        return this.getAllEmployees().stream().filter(employee -> employee.getEmployeeAge() == age).collect(Collectors.toList());
    }
}
