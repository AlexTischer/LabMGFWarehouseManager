package it.polimi.LabMGFwarehousemanager.enums;

import com.google.gson.annotations.SerializedName;

public enum NotificationType {
    @SerializedName("New User")
    NEW_USER(1),
    @SerializedName("New Request")
    NEW_REQUEST(2),
    @SerializedName("New Report")
    NEW_REPORT(3),
    @SerializedName("New Request Update")
    NEW_REQUEST_UPDATE(4);

    private final int value;
    NotificationType(int value) {
        this.value = value;
    }

    public static NotificationType getNotificationTypeFromInt(int value){
        switch (value) {
            case 1:
                return NotificationType.NEW_USER;
            case 2:
                return NotificationType.NEW_REQUEST;
            case 3:
                return NotificationType.NEW_REPORT;
            case 4:
                return NotificationType.NEW_REQUEST_UPDATE;
        }
        return null;
    }

    public int getValue(){
        return value;
    }

    public String getUrl() {
        switch (this) {
            case NEW_USER:
                return "/admins/manageUsersRoles/manageUserRoles.html";
            case NEW_REQUEST:
                return "/admins/manageRequests/manageRequests.html";
            case NEW_REPORT:
                return "/superusers/manageReports/manageReports.html";
            case NEW_REQUEST_UPDATE:
                return "/users/manageRequests/manageRequests.html";
        }
        return null;
    }
}
