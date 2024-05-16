package EmployeeManagement;

import DataBase.DatabaseFunctions;

import java.sql.SQLOutput;

public class Employee {

    private String firstName;
    private String lastName;
    private int age;
    private String position;
    private Double salary;

    private final DatabaseFunctions databaseFunctions = new DatabaseFunctions();

    private static int employeeCount=0;

    public Employee(String firstName, String lastName, int age, String position, Double salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age=age;
        this.position = position;
        this.salary = salary;
        createEmployeeTable(this);
        employeeCount++;
    }



    public Employee(){
        employeeCount++;
        createEmployeeTable(this);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public static int getEmployeeCount() {
        return employeeCount;
    }

//    private void createEmployeeDb(){
//        if(employeeCount==0){
////            System.out.println("Creating Database EmployeeManagement");
////            String createDbQuery="CREATE DATABASE IF NOT EXIST EmployeeManagement";
////            databaseFunctions.executeUpdateStatementDb(createDbQuery);
////            System.out.println("Database EmployeeManagement created successfully");
//
//            System.out.println("Creating Employee Table Now");
//            createEmployeeTable();
//        }
//        else{
//            System.out.println("Database EmployeeManagement already Exists");
//        }
//
//    }

    private void createEmployeeTable(Employee employee){
        if(employeeCount==0) {
            System.out.println("Creating Employee Table ");
            String createEmployeeTableQuery = "CREATE TABLE IF NOT EXISTS EMPLOYEE (id int AUTO_INCREMENT PRIMARY KEY," +
                    "firstName VARCHAR(30), lastName VARCHAR(30),age int,position VARCHAR(30),salary DOUBLE)";

            databaseFunctions.executeUpdateStatementDb(createEmployeeTableQuery);
        }
        saveEmployeeInDb(employee);

    }

    public void saveEmployeeInDb(Employee employee){
        System.out.println("Trying to save employee");
        String saveEmployeeQuery="INSERT INTO EMPLOYEE (FIRSTNAME,LASTNAME,AGE,POSITION,SALARY) VALUES (?,?,?,?,?)";
        databaseFunctions.saveEmployeePreparedStatementDb(saveEmployeeQuery,employee);

    }
}
