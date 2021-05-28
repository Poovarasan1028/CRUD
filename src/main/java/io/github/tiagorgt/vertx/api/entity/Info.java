package io.github.tiagorgt.vertx.api.entity;

import io.vertx.core.json.JsonObject;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "login_info")
public class Info implements Serializable {

    @Id
    @Column(name = "token")
    private String token;

    @Column(name = "login_date")
    private String login_date;
    
    @Column(name = "logout_date")
    private String logout_date;

    @Column(name = "is_active")
    private boolean is_active;

    @Column(name = "username")
    private String username;
    
    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getlogin_date() {
        return login_date;
    }

    public void setlogin_date(String login_date) {
        this.login_date = login_date;
    }
    
    public String getlogout_date() {
        return logout_date;
    }

    public void setlogout_date(String logout_date) {
        this.logout_date = logout_date;
    }
    
    public boolean getis_active() {
        return is_active;
    }

    public void setis_active(boolean is_active) {
        this.is_active = is_active;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toJsonString(){
        return String.valueOf(JsonObject.mapFrom(this));
    }
}
