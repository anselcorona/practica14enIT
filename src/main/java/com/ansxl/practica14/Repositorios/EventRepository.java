package com.ansxl.practica14.Repositorios;

import com.ansxl.practica14.Modelos.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByDate(Date date);
    List<Event> findEventsByDateBetween(Date dateStart, Date dateEnd);

}
