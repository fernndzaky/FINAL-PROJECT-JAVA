package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TableViewController extends Controller implements Initializable {

    //ACCESSING THE FX.ID FROM THE HISTORYTABLE.FXML
    @FXML
    private TableView<HistoryFile> tableView;
    @FXML
    private TableColumn<HistoryFile, String> titleColumn;
    @FXML
    private TableColumn<HistoryFile, String> linkColumn;
    @FXML
    private TableColumn<HistoryFile, LocalDate> dateColumn;


    // AN EMPTY OBSERVABLELIST TO STORE DATA FROM MYSQL
    ObservableList<HistoryFile> history = FXCollections.observableArrayList();

    // INITIALIZER FOR HISTORY TABLE
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // SELECT ALL DATA FROM THE USERS TABLE BASED ON ACTIVE USER AND ADD THE DATA INTO THE OBSERVEABLELIST
        try {
            Connection con = DBConnect.getConnection();
            String sql = String.format("select * from %s" , Main.activeUser);
            ResultSet rs =  con.createStatement().executeQuery(sql);
            while (rs.next()) {
                history.add(new HistoryFile(rs.getString("title"), rs.getString("link"), rs.getString("date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // SET THE COLUMN BASED ON EACH CORRESPONDING DATA
        titleColumn.setCellValueFactory(new PropertyValueFactory<HistoryFile, String>("title"));
        linkColumn.setCellValueFactory(new PropertyValueFactory<HistoryFile, String>("link"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<HistoryFile, LocalDate>("dateAdded"));

        // LOAD THE DATA INTO THE TABLE
        tableView.setItems(history);
    }

    // FUNCTION TO GET THE LINK SO THE USER CAN GIVE THEM TO ANOTHER PEOPLE
    public void copyLink(){

        System.out.println(tableView.getSelectionModel().getSelectedItem().getLink());

        // GET THE SELECTED LINK AND COPY IT INTO CLIPBOARD ON MAC OS
        StringSelection stringSelection = new StringSelection(tableView.getSelectionModel().getSelectedItem().getLink());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    // FUNCTION TO DELETE A ROW FROM THE SELECTED ROW IN THE HISTORY TABLE
    public void deleteRow(){
        // GET THE LINK FROM SELECTED ROW
        String link = tableView.getSelectionModel().getSelectedItem().getLink();
        // DELETE THE ROW FROM THE MYSQL
        deleteRow(link);
        // REMOVE IT FROM THE TABLE IN JAVAFX
        tableView.getItems().remove(tableView.getSelectionModel().getSelectedItem());
    }
}
