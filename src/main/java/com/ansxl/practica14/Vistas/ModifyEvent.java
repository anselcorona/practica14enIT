package com.ansxl.practica14.Vistas;

import com.ansxl.practica14.Modelos.Event;
import com.ansxl.practica14.Servicios.EventService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.calendar.CalendarItemTheme;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

@SpringComponent
@UIScope
public class ModifyEvent extends VerticalLayout {
    TextField title = new TextField("Título");
    DatePicker datePicker = new DatePicker();
    long originalId;
    String originaltitle;
    LocalDate originaldate;
    boolean updated;
    CalendarItemTheme originalcolor;

    public ModifyEvent(@Autowired EventService eventService){
        FormLayout formLayout = new FormLayout();
        H2 header = new H2("Nuevo Correo");
        datePicker.setLabel("Seleccione fecha de evento");
        datePicker.setPlaceholder("Fecha de Evento");
        datePicker.setValue(LocalDate.now());
        Button edit = new Button("Editar");
        edit.getElement().setAttribute("theme", "success");
        Button cancel = new Button("Cancelar");
        cancel.getElement().setAttribute("theme", "error");
        HorizontalLayout actions = new HorizontalLayout(edit, cancel);
        actions.setSpacing(true);
        formLayout.add(title, datePicker);

        edit.addClickListener((e)-> {
                    if (!updated) {
                        Event event = new Event(
                                (long) eventService.eventList().size() + 1,
                                title.getValue(),
                                Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                                originalcolor
                        );
                        try {
                            eventService.createEvent(event);
                            title.setValue("");
                            datePicker.setValue(LocalDate.now());
                            updated = true;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        General.calendar.refresh();
                    }
                }
        );

        cancel.addClickListener( (e)->{
                    Event event = new Event(
                            originalId,
                            originaltitle,
                            Date.from(originaldate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            originalcolor
                    );
                    try{
                        eventService.editEvent(event);
                        title.setValue("");
                        datePicker.setValue(LocalDate.now());
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    General.calendar.refresh();
                }
        );
        add(header, formLayout, actions);
    }
}
