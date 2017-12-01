package mdnaseemashraf.model;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Naseem Ashraf on 17-08-2017.
 */

public class FlashCard {

    private String data;
    private String contextData;
    private String dateCreated;

    private int foreColor;
    private int backColor;

    private long id;

    public FlashCard(String data, String contextData, String dateCreated, int foreColor, int backColor) {
        this.data = data;
        this.contextData = contextData;
        this.dateCreated = dateCreated;
        this.foreColor = foreColor;
        this.backColor = backColor;
    }

    public FlashCard(long id, String data, String contextData, String dateCreated, int foreColor, int backColor) {
        this.id = id;
        this.data = data;
        this.contextData = contextData;
        this.dateCreated = dateCreated;
        this.foreColor = foreColor;
        this.backColor = backColor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getContextData() {
        return contextData;
    }

    public void setContextData(String contextData) {
        this.contextData = contextData;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public int getForeColor() {
        return foreColor;
    }

    public void setForeColor(int foreColor) {
        this.foreColor = foreColor;
    }

    public int getBackColor() {
        return backColor;
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }

}
