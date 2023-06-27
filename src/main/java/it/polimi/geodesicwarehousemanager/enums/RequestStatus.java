package it.polimi.geodesicwarehousemanager.enums;

import com.google.gson.annotations.SerializedName;

public enum RequestStatus {
    @SerializedName("Pending")
    PENDING(0),
    @SerializedName("Approved")
    APPROVED(1),
    @SerializedName("Declined")
    DECLINED(2),
    @SerializedName("Canceled")
    CANCELED(3);
    private final int value;

    RequestStatus(int value) {
        this.value = value;
    }

    /**Returns the Approval Status from an int value*/
    public static RequestStatus getRequestStatusFromInt(int value){
        switch (value) {
            case 0:
                return RequestStatus.PENDING;
            case 1:
                return RequestStatus.APPROVED;
            case 2:
                return RequestStatus.DECLINED;
            case 3:
                return RequestStatus.CANCELED;
        }
        return null;
    }

    public int getValue(){
        return value;
    }
}
