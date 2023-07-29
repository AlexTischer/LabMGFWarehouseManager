package it.polimi.geodesicwarehousemanager.enums;

import com.google.gson.annotations.SerializedName;

public enum DocumentType {
    /*Guida rapida, manuale produttore, sicurezza*/

    @SerializedName("Generic")
    GENERIC(0),
    @SerializedName("Instruction")
    INSTRUCTION(1),
    @SerializedName("Manual")
    MANUAL(2),
    @SerializedName("Safety")
    SAFETY(3),
    @SerializedName("Request")
    REQUEST(4);
    private final int value;

    DocumentType(int value) {
        this.value = value;
    }

    /**Returns the Document Type from an int value*/
    public static DocumentType getDocumentTypeFromInt(int value){
        switch (value) {
            case 0:
                return DocumentType.GENERIC;
            case 1:
                return DocumentType.INSTRUCTION;
            case 2:
                return DocumentType.MANUAL;
            case 3:
                return DocumentType.SAFETY;
            case 4:
                return DocumentType.REQUEST;
        }
        return null;
    }

    public int getValue(){
        return value;
    }
}
