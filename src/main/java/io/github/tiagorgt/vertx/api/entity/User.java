package io.github.tiagorgt.vertx.api.entity;

import org.hibernate.annotations.Columns;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by tiago on 07/10/2017.
 */

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @Column(unique = true, name = "name")
    private String name;

    @Column(name = "email", nullable=false)
    private String email;

    @Column(name = "username", nullable=false)
    private String username;

    @Column(name = "password", nullable=false)
    private String password;
    
    @Column(name = "phone", nullable=false)
    private String phone;
    
    @Column(name = "department")
    private String department;
    

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private Position position;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

}
