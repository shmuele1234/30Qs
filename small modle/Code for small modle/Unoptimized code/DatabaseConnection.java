import java.sql.*;

public class DatabaseConnection {
  private Connection con;
  
  public DatabaseConnection() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      con = DriverManager.getConnection("jdbc:mysql://localhost:3306/30Qs_db", "root", "48xZjVxzsdvAzAVP");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public Connection getConnection() {
    return con;
  }
  
  public void closeConnection() {
    try {
      con.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
