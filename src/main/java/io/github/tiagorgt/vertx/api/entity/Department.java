package io.github.tiagorgt.vertx.api.entity;

import io.vertx.core.json.JsonObject;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "departments")
public class Department implements Serializable {

    @Id
    @Column(name = "branch")
    private String branch;

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String toJsonString(){
        return String.valueOf(JsonObject.mapFrom(this));
    }
}
