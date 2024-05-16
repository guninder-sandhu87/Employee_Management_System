package DataBase;

import EmployeeManagement.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.sql.*;


public class DatabaseFunctions {

    private Connection con;
    private static final Logger logger = LogManager.getLogger();


    private void connectDb() {
        String url = "jdbc:mysql://localhost:3306/EmployeeManagement";
        String userName = "root";
        String password = "password";

        try {
            logger.info("Trying to connect to {}", url);
            con = DriverManager.getConnection(url, userName, password);
            logger.info("Connection Successful");
        } catch (SQLException e) {
            System.out.println("Unable to get connection due to " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void executeUpdateStatementDb(String query) {
        Statement st = null;
        try {
            connectDb();
            st = con.createStatement();
            st.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Unable to execute Statement  " + query + " ---  " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeStatementAndConnection(st);
        }

    }

    public void executeStatementDb(String query) {
        Statement st = null;
        try {
            connectDb();
            st = con.createStatement();
            st.execute(query);
        } catch (SQLException e) {
            System.out.println("Unable to execute statement " + st + " -" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeStatementAndConnection(st);
        }

    }

    private void closeStatementAndConnection(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            System.out.println("Unable to close the statement-" + e.getMessage());
            e.printStackTrace();
        }
        try {
            if (con != null) {
                logger.info("Closing db connection");
                con.close();
                logger.info("closed");
            }
        } catch (SQLException e) {
            System.out.println("Unable to close the connection-" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void closePreparedStatementAndConnection(PreparedStatement preparedStatement) {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            System.out.println("Unable to close prepared statement " + preparedStatement + "--" + e.getMessage());
            e.printStackTrace();
        }
        try {
            if (con != null) {
                logger.info("Closing db connection");
                con.close();
                logger.info("closed");
            }
        } catch (SQLException e) {
            System.out.println("Unable to close the connection-" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveEmployeePreparedStatementDb(String query, Employee employee) {
        connectDb();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setInt(3, employee.getAge());
            preparedStatement.setString(4, employee.getPosition());
            preparedStatement.setDouble(5, employee.getSalary());
            int rows = preparedStatement.executeUpdate();
            System.out.println("Inserted " + rows + " row");
        } catch (SQLException e) {
            System.out.println("Unable to execute prepare statement " + preparedStatement + " --" + e.getMessage());
        } finally {
            closePreparedStatementAndConnection(preparedStatement);
        }

    }

    public void modifyEmployeePreparedStatementDb(String query,int id, String firstName, String lastName, int age, String position, double salary) {
        connectDb();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.setString(4, position);
            preparedStatement.setDouble(5,salary);
            preparedStatement.setInt(6,id);
            int rows = preparedStatement.executeUpdate();
            System.out.println("Updated " + rows + " row");
        } catch (SQLException e) {
            System.out.println("Unable to execute prepare statement " + preparedStatement + " --" + e.getMessage());
        } finally {
            closePreparedStatementAndConnection(preparedStatement);
        }

    }

    public void searchEmployeeFromEmployeeID(int empId){
        connectDb();
        String query = "SELECT * from EMPLOYEE where id=?";
        PreparedStatement preparedStatement=null;
        try{
          preparedStatement = con.prepareStatement(query);
          preparedStatement.setInt(1,empId);
          ResultSet resultSet = preparedStatement.executeQuery();
          if(!resultSet.isBeforeFirst()){
              System.out.println("###################################################");
              System.out.println("  No employee found with id - "+empId);
              System.out.println("####################################################");
              return;
          }
          retrieveResultSet(resultSet);

        }
        catch(SQLException e){
                logger.error("Unable to execute select statement {}",query,e);

        }
        finally {
            closePreparedStatementAndConnection(preparedStatement);
        }

    }

    public void searchEmployeeFromEmployeeName(String firstName, String lastName){
        connectDb();
        String query = "SELECT * from EMPLOYEE where firstName=? and lastName=?";
        PreparedStatement preparedStatement=null;
        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1,firstName);
            preparedStatement.setString(2,lastName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.isBeforeFirst()){
                System.out.println("###########################################################");
                System.out.println("  No employee found with name - "+firstName + " "+ lastName);
                System.out.println("###########################################################");
                return;
            }
            retrieveResultSet(resultSet);

        }
        catch(SQLException e){
            logger.error("Unable to execute select statement {}",query,e);

        }
        finally {
            closePreparedStatementAndConnection(preparedStatement);
        }

    }

    public void retrieveResultSet(ResultSet resultSet){

        try{
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String name= firstName+" "+lastName;
                int age = resultSet.getInt("age");
                String position = resultSet.getString("position");
                double Salary = resultSet.getDouble("salary");
                System.out.println("################################################");
                System.out.println("   Employee Details");
                System.out.println("   -------------------");
                System.out.println("   Id      -> "+ id +"\n   Name    -> "+name+"\n   age     -> "+age + "\n   position-> "+position+ "\n   Salary -> "+Salary);
                System.out.println("################################################\n");
            }
        }catch(SQLException e){
            logger.error("Unable to retrieve info from ResultSet",e);
        }
    }
}
