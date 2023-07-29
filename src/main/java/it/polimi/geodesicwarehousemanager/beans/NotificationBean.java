package it.polimi.geodesicwarehousemanager.beans;

import it.polimi.geodesicwarehousemanager.enums.NotificationType;

public class NotificationBean {
    private int id;
    private int status;
    private NotificationType type;
    private String url;
    private String message;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public NotificationType getType() {
        return type;
    }
    public void setType(NotificationType type) {
        this.type = type;
        setUrl(type.getUrl());}

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

}
