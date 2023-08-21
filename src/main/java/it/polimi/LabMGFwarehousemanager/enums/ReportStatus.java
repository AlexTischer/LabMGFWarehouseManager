package it.polimi.LabMGFwarehousemanager.enums;

import com.google.gson.annotations.SerializedName;

public enum ReportStatus {
    @SerializedName("Pending")
    PENDING(0),
    @SerializedName("Confirmed")
    CONFIRMED(1),
    @SerializedName("Solved")
    SOLVED(2),
    @SerializedName("Canceled")
    CANCELED(3);
    private final int value;

    ReportStatus(int value) {
        this.value = value;
    }

    /**Returns the Approval Status from an int value*/
    public static ReportStatus getReportStatusFromInt(int value){
        switch (value) {
            case 0:
                return ReportStatus.PENDING;
            case 1:
                return ReportStatus.CONFIRMED;
            case 2:
                return ReportStatus.SOLVED;
            case 3:
                return ReportStatus.CANCELED;
        }
        return null;
    }

    public int getValue(){
        return value;
    }
}
