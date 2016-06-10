package com.company.restaurant.dao.hibernate;

import com.company.restaurant.dao.EmployeeDao;
import com.company.restaurant.dao.hibernate.proto.HDaoEntity;
import com.company.restaurant.model.Employee;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Yevhen on 10.06.2016.
 */
public class HEmployeeDao extends HDaoEntity<Employee> implements EmployeeDao {
    @Override
    protected void initMetadata() {

    }

    @Transactional
    @Override
    public Employee addEmployee(Employee employee) {
        return save(employee);
    }

    @Transactional
    @Override
    public String delEmployee(Employee employee) {
        return delete(employee);
    }

    @Transactional
    @Override
    public String delEmployee(int employeeId) {
        return delete(employeeId);
    }

    @Transactional
    @Override
    public Employee findEmployeeById(int employeeId) {
        return findObjectById(employeeId);
    }

    @Transactional
    @Override
    public List<Employee> findEmployeeByFirstName(String firstName) {
        return null;
    }

    @Transactional
    @Override
    public List<Employee> findEmployeeBySecondName(String lastName) {
        return null;
    }

    @Transactional
    @Override
    public List<Employee> findEmployeeByFirstAndSecondName(String firstName, String secondName) {
        return null;
    }

    @Transactional
    @Override
    public List<Employee> findAllEmployees() {
        return findAllObjects();
    }
}
