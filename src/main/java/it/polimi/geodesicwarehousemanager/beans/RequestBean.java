package it.polimi.geodesicwarehousemanager.beans;

import java.sql.Timestamp;
import java.util.ArrayList;

import it.polimi.geodesicwarehousemanager.enums.RequestStatus;

public class RequestBean {
    private int id;
    private String start;
    private String end;
    private RequestStatus status;
    private String reason;
    private UserBean requestingUser;
    private ArrayList<ItemBean> requestedItems;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }
    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }
    public void setEnd(String end) {
        this.end = end;
    }

    public RequestStatus getStatus() {
        return status;
    }
    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }

    public UserBean getRequestingUser() {
        return requestingUser;
    }
    public void setRequestingUser(UserBean requestingUser) {
        this.requestingUser = requestingUser;
    }

    public ArrayList<ItemBean> getRequestedItems() {
        return requestedItems;
    }
    public void setRequestedItems(ArrayList<ItemBean> requestedItems) {
        this.requestedItems = requestedItems;
    }

}
