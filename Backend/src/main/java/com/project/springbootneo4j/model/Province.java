package com.project.springbootneo4j.model;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import java.io.Serializable;

@NodeEntity("Province")
public class Province implements Serializable {
    public static final String className = "province";

    @Id
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Province() {
    }
}
