package EmployeeManagement;

import DataBase.DatabaseFunctions;

import java.util.ArrayList;
import java.util.Scanner;

public class Driver {


    public static void main(String[] args) {

        Driver driver = new Driver();
        Scanner scan = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Welcome to the Dawat-e-mildura EmployeeManagement.\n Choose from below menu");
            System.out.println("1) Add a new employee");
            System.out.println("2) View the details of an employee");
            System.out.println("3) Modify details of an existing employee");
            System.out.println("4) Delete an employee");
            System.out.println("5) Exit");
            choice = scan.nextInt();
            scan.nextLine();
            switch (choice) {
                case 1:
                    driver.takeNewEmployeeDetails(scan);
                    break;
                case 2:
                    System.out.println("How will you like to search the Employee");
                    driver.subMenu(scan, true, false, false);
                    break;
                case 3:
                    System.out.println("Search the Employee whose details you want to modify");
                    driver.subMenu(scan, false, true, false);
                    break;
                case 4:
                    System.out.println("Search the Employee whom you want to delete");
                    driver.subMenu(scan, false, false, true);
                    break;
                case 5:
                    System.out.println("Thanks for using .Bye");
                    break;
                default:
                    System.out.println("Wrong choice");
            }

        } while (choice != 5);
    }


    private void subMenu(Scanner scan, boolean onlyView, boolean modify, boolean delete) {
        System.out.println("Choose:\n1)If you know the id of the Employee.\n2)If you would like to search for employee using name");
        int subChoice = scan.nextInt();
        scan.nextLine();
        DatabaseFunctions databaseFunctions = new DatabaseFunctions();
        Employee employee=null;

        switch (subChoice) {
            case 1:
                System.out.println("Enter the Employee Id");
                int empId = scan.nextInt();
                scan.nextLine();
                employee = databaseFunctions.searchEmployeeFromEmployeeID(empId);
                System.out.println(employee);
                if (modify) {
                    modifyEmployeeDetails(scan,employee);
                } else if (delete) {
                    deleteEmployeeDetails( employee.getId());

                }
                break;
            case 2:
                ArrayList<Employee> employeeList = viewEmployeeDetailsUsingName(scan);
                if (!onlyView) {
                    if (!employeeList.isEmpty()) {
                        System.out.println("Choose the id of the employee");
                        int modifyId = scan.nextInt();
                        scan.nextLine();
                        for (Employee emp:employeeList ) {
                            if(emp.getId()==modifyId){
                                employee=emp;
                            }
                        }

                        if (modify) {
                            modifyEmployeeDetails(scan,employee);
                        } else if (delete) {
                            deleteEmployeeDetails( modifyId);

                        }
                    }
                }
                break;
            default:
                System.out.println("wrong choice in sub Menu");
        }
    }

    private void modifyEmployeeDetails(Scanner scan, Employee employee) {

        DatabaseFunctions databaseFunctions = new DatabaseFunctions();

        boolean done = false;
        do {
            System.out.println("What will you like to modify?");
            System.out.println("1) First Name\n2) Last Name\n3)age\n4)position\n5)Salary\n6)Exit this submenu/ if you have chosen all the changes then press 6");
            int choice = scan.nextInt();
            scan.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("Enter the first name");
                    employee.setFirstName(scan.nextLine());
                    break;
                case 2:
                    System.out.println("Enter the last name");
                    employee.setLastName(scan.nextLine());
                    break;
                case 3:
                    System.out.println("Enter the age");
                    employee.setAge(scan.nextInt());
                    scan.nextLine();
                    break;
                case 4:
                    System.out.println("Enter the position");
                    employee.setPosition(scan.nextLine());
                    break;
                case 5:
                    System.out.println("Enter the Salary");
                    employee.setSalary(scan.nextDouble());
                    break;
                case 6:
                    done = true;
                    break;
                default:
                    System.out.println("invalid");
            }

        } while (!done);

        String query = "UPDATE EMPLOYEE SET firstName=?,lastName=?,age=?,position=?,Salary=? where id=?";
        databaseFunctions.modifyEmployeePreparedStatementDb(query, employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getAge(), employee.getPosition(), employee.getSalary());
    }

    private void deleteEmployeeDetails( int modifyId) {
        DatabaseFunctions databaseFunctions = new DatabaseFunctions();
        System.out.println("Going to delete employee with employee id" + modifyId);
        String deleteQuery = "DELETE FROM EMPLOYEE where id=?";
        databaseFunctions.deleteEmployeeWithId(deleteQuery, modifyId);
    }


    private void takeNewEmployeeDetails(Scanner scan) {
        System.out.println("Enter the first name of employee");
        String firstName = scan.nextLine();
        System.out.println("Enter the last name of employee");
        String lastName = scan.nextLine();
        int age = validateAge(scan);
        scan.nextLine();
        System.out.println("Enter the position");
        String position = scan.nextLine();
        System.out.println("Enter the salary");
        Double Salary = scan.nextDouble();
        createEmployeeObject(firstName, lastName, age, position, Salary);
    }

    private void createEmployeeObject(String firstName, String lastName, int age, String position, Double salary) {
        Employee employee = new Employee(firstName, lastName, age, position, salary);
    }

    private int validateAge(Scanner scan) {
        System.out.println("Enter the age");
        int age = scan.nextInt();
        if (age >= 18 && age <= 65) {
            return age;
        } else {
            System.out.println("invalid Age : age should be between 18 and 65");
            return validateAge(scan);
        }
    }

    private ArrayList<Employee> viewEmployeeDetailsUsingName(Scanner scan) {
        ArrayList<Employee> employee = new ArrayList<>();
        DatabaseFunctions databaseFunctions = new DatabaseFunctions();
        System.out.println("Enter the Employee first Name");
        String firstName = scan.nextLine();
        System.out.println("Enter the last Name");
        String lastName = scan.nextLine();
        employee = databaseFunctions.searchEmployeeFromEmployeeName(firstName, lastName);
        printEmployee(employee);

        return employee;
    }

    private void printEmployee(ArrayList<Employee> employee) {
        if (employee.isEmpty()) {
            System.out.println("###################################################");
            System.out.println("  No employee found ");
            System.out.println("####################################################");
        } else {
            employee.forEach(System.out::println);
        }
    }
}