package dev.nacho.ghub.domain.model.dto;

public class GoogleUserInfo {
    private String sub;
    private String email;
    private String name;

    // Getters y setters
    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
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
}