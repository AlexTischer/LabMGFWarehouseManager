package it.polimi.geodesicwarehousemanager.beans;

import it.polimi.geodesicwarehousemanager.enums.ReportStatus;

import java.sql.Timestamp;
import java.util.ArrayList;

public class ReportBean {
    private int id;
    private Timestamp date;
    private UserBean reportingUser;
    private ArrayList<ItemBean> reportedItems;
    private String subject;
    private String body;
    private ReportStatus status;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }
    public void setDate(Timestamp date) {
        this.date = date;
    }

    public UserBean getReportingUser() {
        return reportingUser;
    }
    public void setReportingUser(UserBean reportingUser) {
        this.reportingUser = reportingUser;
    }

    public ArrayList<ItemBean> getReportedItems() {
        return reportedItems;
    }
    public void setReportedItems(ArrayList<ItemBean> reportedItems) {
        this.reportedItems = reportedItems;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }

    public ReportStatus getStatus() {
        return status;
    }
    public void setStatus(ReportStatus reportStatus) {
        this.status = reportStatus;
    }
}
