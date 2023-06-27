package it.polimi.geodesicwarehousemanager.beans;

import java.sql.Timestamp;
import java.util.ArrayList;

import it.polimi.geodesicwarehousemanager.enums.RequestStatus;

public class RequestBean {
    private int id;
    private Timestamp start;
    private Timestamp end;
    private RequestStatus requestStatus;
    private String reason;
    private int userId;
    private ArrayList<Integer> tools;
    private ArrayList<Integer> accessories;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getStart() {
        return start;
    }
    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }
    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }
    public void setApprovalStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ArrayList<Integer> getTools() {
        return tools;
    }
    public void setTools(ArrayList<Integer> tools) {
        this.tools = tools;
    }

    public ArrayList<Integer> getAccessories() {
        return accessories;
    }
    public void setAccessories(ArrayList<Integer> accessories) {
        this.accessories = accessories;
    }
}
