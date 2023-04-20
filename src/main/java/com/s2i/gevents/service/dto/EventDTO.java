package com.s2i.gevents.service.dto;


import lombok.ToString;

import java.io.Serializable;

@ToString
public class EventDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String type;

    private Boolean active;

    private Integer first;
    private Integer second;

    public EventDTO() {

    }


    public EventDTO(String type, Boolean active, Integer first, Integer second){
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
