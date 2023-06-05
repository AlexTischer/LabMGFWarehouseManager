package it.polimi.geodesicwarehousemanager.enums;

import com.google.gson.annotations.SerializedName;

public enum ItemType {
    @SerializedName("Tool")
    TOOL(0),
    @SerializedName("Accessory")
    ACCESSORY(1),
    @SerializedName("Document")
    DOCUMENT(2);

    private final int value;
    ItemType(int value) {
        this.value = value;
    }

    public static ItemType getItemTypeFromInt(int value){
        switch (value) {
            case 0:
                return ItemType.TOOL;
            case 1:
                return ItemType.ACCESSORY;
            case 2:
                return ItemType.DOCUMENT;
        }
        return null;
    }

    public int getValue(){
        return value;
    }
}
