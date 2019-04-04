package com.ansxl.practica14.Vistas;

import com.ansxl.practica14.Modelos.Event;
import com.ansxl.practica14.Modelos.User;
import com.ansxl.practica14.Servicios.EventService;
import com.ansxl.practica14.Servicios.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Route("main")
@SpringComponent
@UIScope
public class General extends VerticalLayout {
    public static CalendarComponent<Event> calendar = new CalendarComponent<Event>()
            .withItemDateGenerator(Event::getDate)
            .withItemLabelGenerator(Event::getName)
            .withItemThemeGenerator(Event::getColor);

    @Autowired
    public static EventService eventService;

    @Autowired
    public General(@Autowired Email emailView,
                   @Autowired EventView eventView,
                   @Autowired ModifyEvent modifyEvent,
                   @Autowired UserService userService,
                   @Autowired EventService eventService){
        General.eventService = eventService;
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

            Button NewEvent = new Button("Crear Evento");
            Button email = new Button("Enviar Correo Electrónico");
            Button userInfo = new Button("Gestionar Usuarios");
            Button logout = new Button("Cerrar Sesión");
            email.getElement().setAttribute("theme", "success");
            NewEvent.getElement().setAttribute("theme","primary");
            userInfo.getElement().setAttribute("theme", "error");
            logout.getElement().setAttribute("theme", "primary");
            menu = new HorizontalLayout(NewEvent, email, userInfo, logout);

            email.addClickListener((e)->{
                Dialog dialog = new Dialog();
                dialog.add(emailView);
                dialog.open();
            });
            NewEvent.addClickListener((e)->{
                Dialog dialog = new Dialog();
                dialog.add(eventView);
                dialog.open();
            });
            userInfo.addClickListener((event -> getUI().get().navigate("user")));
            logout.addClickListener((e)->{
                try {
                    User u = userService.userList().get(0);
                    u.setLoggedIn(false);
                    userService.editUser(u);
                    getUI().get().navigate("");
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });

            H3 title = new H3("Práctica 14 en IT");
            H5 screen = new H5("Ventana General");
            eventService.createEvent(
                    1,
                    "Nuevo Evento",
                    Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().plus(Period.ofDays(1))),
                    CalendarItemTheme.Green
            );
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date d = sdf.parse("29/07/2019");
                eventService.createEvent(
                        2,
                        "Mi cumpleaños",
                        d,
                        CalendarItemTheme.Red
                );
                Date date = sdf.parse("03/04/2019");
                eventService.createEvent(3,
                        "#TesisDone",
                        date,
                        CalendarItemTheme.Black);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            calendar.setDataProvider(new CalendarDataProvider());
            calendar.addEventClickListener(evento ->{
                eventService.deleteEvent(evento.getDetail());
                modifyEvent.datePicker.setValue(evento.getDetail().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                modifyEvent.title.setValue(evento.getDetail().getName());
                modifyEvent.originaltitle = evento.getDetail().getName();
                modifyEvent.originaldate = evento.getDetail().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                modifyEvent.originalcolor = evento.getDetail().getColor();
                modifyEvent.originalId = evento.getDetail().getId();
                modifyEvent.updated = false;
                Dialog dialog = new Dialog();
                dialog.add(modifyEvent);
                dialog.open();
            });
            add(title, screen, menu, calendar);
        }
    }
}

@SpringComponent
@UIScope
class CalendarDataProvider extends AbstractCalendarDataProvider<Event> {
    @Override
    public Collection<Event> getItems(Date fromDate, Date toDate) {
        List<Event> eventos = General.eventService.eventList();
        return eventos;
    }
}
