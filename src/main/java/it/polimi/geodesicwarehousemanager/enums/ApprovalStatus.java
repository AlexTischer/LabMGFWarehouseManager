package it.polimi.geodesicwarehousemanager.enums;

import com.google.gson.annotations.SerializedName;

public enum ApprovalStatus {
    @SerializedName("Pending")
    PENDING(0),
    @SerializedName("Approved")
    APPROVED(1),
    @SerializedName("Declined")
    DECLINED(2);
    private final int value;

    ApprovalStatus(int value) {
        this.value = value;
    }

    /**Returns the Approval Status from an int value*/
    public static ApprovalStatus getApprovalStatusFromInt(int value){
        switch (value) {
            case 0:
                return ApprovalStatus.PENDING;
            case 1:
                return ApprovalStatus.APPROVED;
            case 2:
                return ApprovalStatus.DECLINED;
        }
        return null;
    }

    public int getValue(){
        return value;
    }
}
