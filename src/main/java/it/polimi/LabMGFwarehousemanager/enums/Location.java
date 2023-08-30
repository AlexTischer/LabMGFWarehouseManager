package it.polimi.LabMGFwarehousemanager.enums;

import com.google.gson.annotations.SerializedName;

public enum Location {
    @SerializedName("Milano-Leonardo")
    MILANO_LEONARDO(0),
    @SerializedName("Piacenza")
    Piacenza(1);

    private final int value;
    Location(int value) {
        this.value = value;
    }

    public static Location getLocationFromInt(int value){
        switch (value) {
            case 0:
                return Location.MILANO_LEONARDO;
            case 1:
                return Location.Piacenza;
        }
        return null;
    }

    public int getValue(){
        return value;
    }
}
