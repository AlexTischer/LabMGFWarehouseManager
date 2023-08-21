package it.polimi.LabMGFwarehousemanager.beans;

import it.polimi.LabMGFwarehousemanager.enums.DocumentLanguage;
import it.polimi.LabMGFwarehousemanager.enums.DocumentType;

public class DocumentBean {
    private int id;
    private String name;
    private DocumentType type;
    private DocumentLanguage language;
    private String path;
    private boolean isLinked;

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

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    public DocumentLanguage getLanguage() {
        return language;
    }

    public void setLanguage(DocumentLanguage language) {
        this.language = language;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isLinked() {
        return isLinked;
    }

    public void setIsLinked(boolean linked) {
        isLinked = linked;
    }
}
