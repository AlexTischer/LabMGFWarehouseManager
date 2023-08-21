package it.polimi.LabMGFwarehousemanager.beans;

import it.polimi.LabMGFwarehousemanager.enums.Location;

public class ItemBean {
    private int id;
    private String name;
    private String description;
    private int type;
    private String serialNumber;
    private String inventoryNumber;
    private Location location;
    private String imagePath;
    private boolean isLinked;
    private boolean isInMaintenance;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }
    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public int getLocation() {
        return location.getValue();
    }
    public void setLocation(int location) {
        this.location = Location.getLocationFromInt(location);
    }

    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean getIsLinked() {
        return isLinked;
    }
    public void setIsLinked(boolean isLinked) {
        this.isLinked = isLinked;
    }

    public boolean getIsInMaintenance() {
        return isInMaintenance;
    }
    public void setIsInMaintenance(boolean isInMaintenance) {
        this.isInMaintenance = isInMaintenance;
    }
}
