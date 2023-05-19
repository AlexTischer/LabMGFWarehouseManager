package it.polimi.geodesicwarehousemanager.beans;

import it.polimi.geodesicwarehousemanager.enums.UserRole;

public class UserBean {
    private int id;
    private String email;
    private String name;
    private String surname;
    private String password;
    private UserRole role;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }
    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserBean clone(){
        UserBean cloned = new UserBean();
        cloned.setId(this.id);
        cloned.setEmail(this.email);
        cloned.setName(this.name);
        cloned.setPassword(this.password);
        cloned.setRole(this.role);
        return cloned;
    }

}
