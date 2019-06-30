package sample;
import java.sql.*;

public class DBConnect {

    public Connection con;
    public Statement st;

    // FUNCTIONS TO RETURN THE CONNECTION TO THE DATABASE
    public static Connection getConnection() throws SQLException{

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finaljava?useTimezone=true&serverTimezone=UTC","root","");

        return connection;

    }

    // CONSTRUCTOR FOR DBCONNECT THAT CONNECTS JAVA WITH MYSQL
    DBConnect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Create Connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/finaljava?useTimezone=true&serverTimezone=UTC","root","");
            //"jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12294102","sql12294102","YrBYNJeaNR"
            st = con.createStatement();
        }
        catch (Exception e){
            System.out.println("Error : " + e);
        }
    }

    // FUNCTION TO INSERT DATA INTO HISTORY
    void insertData( String title, String link , String date){
        // INSERT DATA INTO HISTORY OF THE LOGGED IN USER
        String sql = String.format("insert into %s(title,link,date) VALUES('%s', '%s' ,'%s')"  , Main.activeUser, title ,link,date);
        //System.out.println(sql);
        try {
            st = con.createStatement();
            st.executeUpdate(sql);
            System.out.println("Data Inserted");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    // FUNCTION TO DELETE A HISTORY
    void deleteRow(String link){
        // DELETE A ROW FROM THE HISTORY OF THE LOGGED IN USER
        String sql = String.format("delete from %s where link = '%s' " , Main.activeUser ,link);

        try {
            st = con.createStatement();
            st.executeUpdate(sql);
            System.out.println("Row Deleted");
        } catch (Exception e){
            System.out.println(e);
        }
    }

}
