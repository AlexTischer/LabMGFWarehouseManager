package it.polimi.geodesicwarehousemanager.enums;

import com.google.gson.annotations.SerializedName;

public enum UserRole {
    @SerializedName("Unregistered")
    UNREGISTERED(-1),
    @SerializedName("None")
    NONE(0),
    @SerializedName("User")
    USER(1),
    @SerializedName("SuperUser")
    SUPERUSER(2),
    @SerializedName("Admin")
    ADMIN(3);
    private final int value;

    UserRole(int value) {
        this.value = value;
    }

    /**Returns the User Role from an int value*/
    public static UserRole getUserRoleFromInt(int value){
        switch (value) {
            case -1:
                return UserRole.UNREGISTERED;
            case 0:
                return UserRole.NONE;
            case 1:
                return UserRole.USER;
            case 2:
                return UserRole.SUPERUSER;
            case 3:
                return UserRole.ADMIN;
        }
        return null;
    }

    public int getValue(){
        return value;
    }
}
