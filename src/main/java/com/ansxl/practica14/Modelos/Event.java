package com.ansxl.practica14.Modelos;

import org.vaadin.calendar.CalendarItemTheme;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Event implements Serializable {

    @Id
    private long id;
    private String name;
    private Date date;
    private CalendarItemTheme color;


    public Event(long id, String name, Date date, CalendarItemTheme color) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public CalendarItemTheme getColor() {
        return color;
    }

    public void setColor(CalendarItemTheme color) {
        this.color = color;
    }
}
