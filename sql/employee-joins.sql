SELECT * FROM employee;
SELECT * FROM employee_2;
SELECT * FROM employee_3;

SELECT e.id, e.name, e2.age, e3.primary_skill FROM 
employee e
LEFT JOIN employee_2 e2
ON e.id = e2.id
LEFT JOIN employee_3 e3
ON e.id = e3.id;
