package com.s2i.gevents.domain;


import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@ToString
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private Boolean active;

    private Integer first;
    private Integer second;

    public Event() {

    }


    public Event(String type, Boolean active, Integer first, Integer second){
        this.type = type;
        this.active = active;
        this.first = first;
        this.second = second;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getFirst() {
        return first;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

}
