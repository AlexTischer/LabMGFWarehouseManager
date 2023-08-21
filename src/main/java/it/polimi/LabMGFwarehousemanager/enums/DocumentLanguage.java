package it.polimi.LabMGFwarehousemanager.enums;

import com.google.gson.annotations.SerializedName;

public enum DocumentLanguage {
    @SerializedName("Italian")
    ITALIAN(0),
    @SerializedName("English")
    ENGLISH(1);
    private final int value;

    DocumentLanguage(int value) {
        this.value = value;
    }

    /**Returns the Document Language from an int value*/
    public static DocumentLanguage getDocumentLanguageFromInt(int value){
        switch (value) {
            case 0:
                return DocumentLanguage.ITALIAN;
            case 1:
                return DocumentLanguage.ENGLISH;
        }
        return null;
    }

    public int getValue(){
        return value;
    }
}
