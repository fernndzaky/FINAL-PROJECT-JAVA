package sample;

import javafx.beans.property.SimpleStringProperty;

public class HistoryFile {

    // ATTRIBUTES FOR HISTORY FILE
    private SimpleStringProperty title , link , dateAdded;

    // CONSTRUCTOR FOR HISTORY FILE
    public HistoryFile(String title , String link , String dateAdded){
        this.title = new SimpleStringProperty(title);
        this.link = new SimpleStringProperty(link);
        this.dateAdded = new SimpleStringProperty(dateAdded);
    }

    // SETTER GETTER FOR HISTORY FILE
    public String getTitle(){
        return title.get();
    }

    public String getLink(){
        return link.get();
    }

    public String getDateAdded(){
        return dateAdded.get();
    }

    public void setTitle(SimpleStringProperty title){
        this.title = title;
    }

    public void setLink(SimpleStringProperty link){
        this.link = link;
    }

    public void setDateAdded(SimpleStringProperty dateAdded){
        this.dateAdded = dateAdded;
    }

}
