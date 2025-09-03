package com.bajajfinserv.webhookqualifier.service;

import org.springframework.stereotype.Service;

@Service
public class SqlSolutionService {
    
    public String getSqlSolution(String regNo) {
        String lastTwoDigits = regNo.substring(regNo.length() - 2);
        int lastTwoDigitsInt = Integer.parseInt(lastTwoDigits);
        
        if (lastTwoDigitsInt % 2 == 1) {
            return getSolutionForQuestion1();
        } else {
            return getSolutionForQuestion2();
        }
    }
    
    private String getSolutionForQuestion1() {
        // Sample solution for Question 1 (you need to replace with actual)
        return """
            SELECT 
                e.employee_id,
                e.first_name,
                e.last_name,
                d.department_name,
                e.salary
            FROM employees e
            JOIN departments d ON e.department_id = d.department_id
            WHERE e.salary > (
                SELECT AVG(salary) 
                FROM employees 
                WHERE department_id = e.department_id
            )
            ORDER BY e.salary DESC;
            """;
    }
    
    private String getSolutionForQuestion2() {
        // ACTUAL Question 2 Solution: Calculate younger employees count by department
        return """
            SELECT 
                e1.EMP_ID,
                e1.FIRST_NAME,
                e1.LAST_NAME,
                d.DEPARTMENT_NAME,
                COUNT(e2.EMP_ID) as YOUNGER_EMPLOYEES_COUNT
            FROM EMPLOYEE e1
            JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID
            LEFT JOIN EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT 
                AND e2.DOB > e1.DOB
            GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME
            ORDER BY e1.EMP_ID DESC;
            """;
    }
}