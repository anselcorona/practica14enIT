package com.ansxl.practica14.Servicios;

import com.ansxl.practica14.Modelos.Event;
import com.ansxl.practica14.Repositorios.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.calendar.CalendarItemTheme;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    public List<Event> eventList() { return  eventRepository.findAll();}
    public Event findEventById(long id) { return eventRepository.getOne(id);}

    @Transactional
    public Event createEvent(long id, String name, Date date, CalendarItemTheme color){
        return eventRepository.save(new Event(id, name, date, color));
    }
    @Transactional
    public Event createEvent(Event e){
        return eventRepository.save(e);
    }
}
