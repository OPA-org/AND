package cse.opa.and.Classes;

import java.util.Date;

/**
 * Created by Peter on 28/3/2018.
 */

public class Report {
    private String title;
    private java.util.Date date;

    public Report(String title, Date date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
