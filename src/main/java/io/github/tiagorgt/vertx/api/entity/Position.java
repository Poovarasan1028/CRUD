package io.github.tiagorgt.vertx.api.entity;

import io.vertx.core.json.JsonObject;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "position")
public class Position implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toJsonString(){
        return String.valueOf(JsonObject.mapFrom(this));
    }
}
