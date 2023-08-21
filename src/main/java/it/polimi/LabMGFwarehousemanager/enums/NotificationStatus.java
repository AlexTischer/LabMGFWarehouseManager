package it.polimi.LabMGFwarehousemanager.enums;

import com.google.gson.annotations.SerializedName;

public enum NotificationStatus {
    @SerializedName("Unread")
    UNREAD(0),
    @SerializedName("Read")
    READ(1);
    private final int value;

    NotificationStatus(int value) {
        this.value = value;
    }

    /**Returns the Notification Status from an int value*/
    public static NotificationStatus getNotificationStatusFromInt(int value){
        switch (value) {
            case 0:
                return NotificationStatus.UNREAD;
            case 1:
                return NotificationStatus.READ;
        }
        return null;
    }

    public int getValue(){
        return value;
    }
}
