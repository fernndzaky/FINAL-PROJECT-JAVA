package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class loginController extends DBConnect {

    // ACCESSING THE FX.ID INT HE LOGINPAGE.FMXL
    @FXML
    Button loginButton;

    @FXML
    TextField signUpText;

    @FXML
    Text successRegister;

    @FXML
    Text isTakenText;

    @FXML
    TextField loginText;

    // IF USER CLICK LOGIN BUTTON
    public void login() throws IOException , ClassNotFoundException {
        // GET THE TEXT FROM THE GIVEN TEXTFIELD
        String username = loginText.getText();
        // SET THE COUNTER TO 0
        int counter = 0;

        try {
            Connection con = DBConnect.getConnection();
            // SELECT ALL DATA FROM USERS
            ResultSet rs = con.createStatement().executeQuery("select * from users");
            while (rs.next()) {
                // IF THE USER EXISTS IN THE USERS TABLE IN MYSQL , OPEN THE USER HOME
                if(username.equals(rs.getString("username"))){
                    Main.activeUser = username;
                    Parent root = FXMLLoader.load(getClass().getResource("testDesign.fxml"));
                    Main.primaryStage.setScene(new Scene(root, 600, 400));

                    // SET THE COUNTER INTO 1
                    counter = 1;

                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // IF THE COUNTER IS 0 , THEN THE USER HASNT REGISTERED OR NOT FOUND IN THE USERS TABLE IN MYSQL
        if(counter== 0){
            isTakenText.setText("User is'nt exists !");
            successRegister.setText("");
        }


    }

    // WHEN USER CLICK THE REGISTER BUTTON
    public void register(){
        // GET THE TEXT FROM THE GIVEN TEXTFIELD
        String username = signUpText.getText();

        // SET THE COUNTER TO 0
        int counter = 0;
        try {
            Connection con = DBConnect.getConnection();

            // SELECT ALL DATA FROM USERS
            ResultSet rs = con.createStatement().executeQuery("select * from users");
            while (rs.next()) {
                // IF THE USERS ALREADY EXISTS IN THE USERS TABLE IN MYSQL , THEN USER CANNOT REGISTER
                if(username.equals(rs.getString("username"))){
                    isTakenText.setText("Username is taken !");
                    successRegister.setText("");

                    // SET THE COUNTER INTO 1
                    counter = 1;
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // IF THE COUNTER IS 0  , CREATE A NEW TABLE BASED ON THE FILLED TEXTFIELD
        if(counter==0){
            // CREATE TABLE , WITH ID , TITILE , LINK , AND DATE FOR THE COLUMNS
            String sql = String.format("CREATE TABLE %s (\n" +
                    "    id int NOT NULL AUTO_INCREMENT,\n" +
                    "    title varchar(255),\n" +
                    "    link varchar(255),\n" +
                    "    date varchar(255),\n" +
                    "    PRIMARY KEY(id)\n" +
                    ");" , username) ;

            // INSERT THE FILLED TEXTFIELD INTO USERNAME TABLE IN MYSQL
            String ssql = String.format("insert into users(username) VALUES('%s')" , username);

            try{
                st = con.createStatement();
                st.executeUpdate(sql);
                System.out.println("Table Inserted");
                st.executeUpdate(ssql);
                System.out.println("User Registered");
                successRegister.setText("Account successfully registered !");
                isTakenText.setText("");

            }catch (Exception e){
                e.printStackTrace();
            }


        }
        signUpText.setText("");

    }

}
