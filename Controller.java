package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;

import java.net.URL;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class Controller extends DBConnect implements Initializable {

    // ACCESSING THE FX ID FROM THE FXML
    @FXML
    TextField linkBox;

    // ACCESSING THE FX ID FROM THE FXML
    @FXML
    TextField titleBox;

    // ACCESSING THE FX ID FROM THE FXML
    @FXML
    Text fileName;

    // ACCESSING THE FX ID FROM THE FXML
    @FXML
    Text blankValidation;

    // BLANK STRING FOR SAVING BOTH TITLE AND LINK FROM TEXTFIELD
    String titleAsText = "" ;
    String linkAsText = "";


    // FORMATTING DATE AS DD/MM//YY FOR TAKING THE CURRENT DATE
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyy");
    // TAKING CURRENT DATE
    LocalDate date = LocalDate.now();

    // INITIALIZER FOR THE USER HOME
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        blankValidation.setText("");
        System.out.println(Main.activeUser);
    }


    // SAVING LINK FROM THE UPLOADED FILE INTO HISTORY
    public void saveData() {

        // IF ONE OF THE TEXTFIELD IS NOT FILLED
        if(linkBox.getText().equals("") && titleBox.getText().equals("")){
            blankValidation.setText("  BOTH TITLE AND LINK ARE REQUIRED");
        }
        // IF THE TITLEFIELD IS NOT FILLED
        else if(titleBox.getText().equals("")){

            blankValidation.setText("NO TITLE FOUND , PLEASE FILL IN TITLE");
        }
        // IF THE LINKFIELD IS NOT FILLED
        else if(linkBox.getText().equals("")){
            blankValidation.setText("LINK IS REQUIRED , PLEASE UPLOAD FILE");
        }
        // IF THE LINKFIELD IS INVALID
        else if(!linkBox.getText().contains("https://ufile")){
            blankValidation.setText("INVALID LINK ! , PLEASE UPLOAD FILE");
        }
        // IF EVERY TEXTFIELD IS FILLED
        else{
            // RETURN THE STRING THAT IS FILLED AT THE TITLE FIELD
            titleAsText = titleBox.getText();

            // RETURN THE STRING THAT IS FILLED AT THE LINK FIELD
            linkAsText = linkBox.getText();

            // INSERT DATAS INTO HISTORY TABLE FROM DATABASE
            insertData(titleAsText , linkAsText , dtf.format(date));

            // SETTING ALL VALIDATION AS EMPTY STRING
            titleAsText = "" ;
            linkAsText = "";
            linkBox.setText("");
            titleBox.setText("");
            fileName.setText("File Name :");
            blankValidation.setText("");

        }

    }

    // OPENING CHROME IN ORDER TO DOWNLOAD THE GIVEN LINK
    public void downloadFile() throws Exception {

        // VALIDATION IF THE LINK FIELD IS NOT FILLED
        if (linkBox.getText().equals("")) {
            blankValidation.setText("THERE IS NO LINK , PLEASE UPLOAD FILE");
        }
        // IF THE LINK FIELD IS FILLED
        else {
            // IF THE LINK IS INCORRECT
            if(!linkBox.getText().contains("https://ufile")){
                blankValidation.setText("NO LINK FOUND, PLESE UPLOAD AGAIN");
            }
            // IF THE LINK IS CORRECT
            else{
                // OPENING GOOGLE CHROME
                WebDriver driver = new ChromeDriver();
                // OPENING THE LINK THE GOOGLE CHROME
                driver.navigate().to(linkBox.getText());
                // WAIT FOR 1 MINUTE UNTIL THE LINK IS READY TO DOWNLOAD
                WebDriverWait wait = new WebDriverWait(driver, 7);
                //Thread.sleep(7000);
                System.out.println("udah nunggu");

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/section[1]/div[2]/div/div/div[3]/a[1]")));
                System.out.println("element ada");

                // SEARCH FOR AN ELEMENT IN THE WEBSITE THAT POP UP , IN ORDER TO DOWNLOAD THE FILE
                WebElement upload = driver.findElement(By.xpath("/html/body/section[1]/div[2]/div/div/div[3]/a[1]"));
                System.out.println("element click");

                // CLICK THE BUTTON WHEN ITS FOUND TO DOWNLOAD THE FILE
                upload.click();
                blankValidation.setText("FILE DOWNLOADED");
            }

        }

    }

    // FUNCTIONS TO OPEN HISTORY STAGE
    public void goToHistory() throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("historyTable.fxml"));
        // CREATING NEW STAGE
        Stage historyStage = new Stage();
        historyStage.setScene(new Scene(root, 600, 400));
        historyStage.setResizable(false);
        historyStage.show();
}

    // FUNCTIONS TO LOG OUT CURRENT USER AND GO BACK TO MAIN MENU
    public void logout() throws IOException {
        // SET THE ACTIVE USER INTO EMPTY STRING
        Main.activeUser = "";
        // OPEN LOGINPAGE.FXML
        Parent root = FXMLLoader.load(getClass().getResource("loginPage.fxml"));
        Main.primaryStage.setScene(new Scene(root, 600, 400));

    }

    // FUNCTIONS TO UPLAOD A FILE AND RETURN IT AS A LINK
    public void uploadFile() throws InterruptedException, IOException {

        // OPENING THE MAC FILE CHOOSER SO THE USER CAN CHOOSE A FILE TO UPLOAD
        FileChooser file = new FileChooser();
        File selectedFile = file.showOpenDialog(null);
        //(Thread.sleep(2000);
        // SETTING THE CHROME TO HEADLESS SO THE USER CANNOT SEE THE PROCESS OF OPENING THE BROWSER AND ETC
        ChromeOptions opt = new ChromeOptions();
        opt.addArguments("headless");

        // OPENING GOOGLE CHROME
        WebDriver driver = new ChromeDriver(opt);
        // NAVIGATE THE DRIVER TO UPLOADFILES.IO
        driver.navigate().to("http://uploadfiles.io");

        // SEARCH THE ELEMENT IN THE WEBSITE THAT CAN UPLOAD A FILE
        WebElement upload = driver.findElement(By.xpath("//*[@id=\"upload-window\"]"));

        // DRAG AND DROP SNIPPET CODE FROM INTERNET
        final String JS_DROP_FILE =
                "var tgt=arguments[0],e=document.createElement('input');e.type='" +
                        "file';e.addEventListener('change',function(event){var dataTrans" +
                        "fer={dropEffect:'',effectAllowed:'all',files:e.files,items:{},t" +
                        "ypes:[],setData:function(format,data){},getData:function(format" +
                        "){}};var emit=function(event,target){var evt=document.createEve" +
                        "nt('Event');evt.initEvent(event,true,false);evt.dataTransfer=da" +
                        "taTransfer;target.dispatchEvent(evt);};emit('dragenter',tgt);em" +
                        "it('dragover',tgt);emit('drop',tgt);document.body.removeChild(e" +
                        ");},false);document.body.appendChild(e);return e;";
        JavascriptExecutor js = (JavascriptExecutor)driver;
        WebElement upl = (WebElement)js.executeScript(JS_DROP_FILE, new Object[]{upload});
        upl.sendKeys(selectedFile.getPath());


        // WAIT FOR 60 SECONDS
        WebDriverWait wait = new WebDriverWait(driver, 60);

        // WAIT UNTIL THE LINK IS ALREADY RETURNED FROM THE WEBSITE
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"copylink\"]")));
        // GRAB THE LINK AND CONTAIN THE LINK IN A WEB ELEMENT
        WebElement link = driver.findElement(By.xpath("//*[@id=\"share-file\"]"));
        // SET THE FXML LINK BOX WITH THE GIVEN LINK FROM THE WEBSITE
        linkBox.setText(link.getAttribute("data-url"));
        fileName.setText("File Name : " + selectedFile.getName());
        linkAsText = link.getAttribute("data-url");
        titleAsText = titleBox.getText();
        blankValidation.setText("");

    }


}
