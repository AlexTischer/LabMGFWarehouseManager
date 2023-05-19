package it.polimi.geodesicwarehousemanager.beans;

import java.sql.Timestamp;

public class ReportBean {
    private int id;
    private Timestamp reportDate;
    private int reporterId;
    private int reportedItemId;
    private String object;
    private String message;
    private boolean isSolved;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getReportDate() {
        return reportDate;
    }
    public void setReportDate(Timestamp reportDate) {
        this.reportDate = reportDate;
    }

    public int getReporterId() {
        return reporterId;
    }
    public void setReporterId(int reporterId) {
        this.reporterId = reporterId;
    }

    public int getReportedItemId() {
        return reportedItemId;
    }
    public void setReportedItemId(int reportedItemId) {
        this.reportedItemId = reportedItemId;
    }

    public String getObject() {
        return object;
    }
    public void setObject(String object) {
        this.object = object;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSolved() {
        return isSolved;
    }
    public void setSolved(boolean solved) {
        isSolved = solved;
    }
}
