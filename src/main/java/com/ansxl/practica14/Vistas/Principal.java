package com.ansxl.practica14.Vistas;

import com.ansxl.practica14.Modelos.Event;
import com.ansxl.practica14.Servicios.EventService;
import com.ansxl.practica14.Servicios.UserService;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.calendar.CalendarComponent;
import org.vaadin.calendar.CalendarItemTheme;
import org.vaadin.calendar.data.AbstractCalendarDataProvider;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Route("main")
@SpringComponent
@UIScope
public class Principal extends VerticalLayout {
    public static CalendarComponent<Event> calendar = new CalendarComponent<Event>()
            .withItemDateGenerator(Event::getDate)
            .withItemLabelGenerator(Event::getName)
            .withItemThemeGenerator(Event::getColor);

    @Autowired
    public static EventService eventService;

    @Autowired
    public Principal(@Autowired UserService userService, @Autowired EventService eventService){
        Principal.eventService = eventService;
        if(userService.userList().isEmpty()){
            getUI().get().navigate("");
        }
      else if(!userService.userList().get(0).isLoggedIn()){
            getUI().get().navigate("");
        }
        else {
            setAlignItems(Alignment.CENTER);
            HorizontalLayout menu = new HorizontalLayout();
            menu.setSpacing(true);
            H3 title = new H3("Práctica 14 en IT");
            H5 screen = new H5("Main");
            eventService.createEvent(
                    1,
                    "Nuevo Evento",
                    Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().plus(Period.ofDays(1))),
                    CalendarItemTheme.Green
            );
            calendar.setDataProvider(new ProveedorDatosCalendario());
            add(title, screen,calendar);
        }
    }
}

@SpringComponent
@UIScope
class ProveedorDatosCalendario extends AbstractCalendarDataProvider<Event> {
    @Override
    public Collection<Event> getItems(Date fromDate, Date toDate) {
        List<Event> eventos = Principal.eventService.eventList();
        return eventos;
    }
}
