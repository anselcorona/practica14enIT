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
public class EventView extends VerticalLayout {
    TextField title = new TextField("Título");
    DatePicker datePicker = new DatePicker();

    public EventView(@Autowired EventService eventService){
        FormLayout formLayout = new FormLayout();
        H2 header = new H2("Nuevo Correo");
        datePicker.setLabel("Seleccione fecha de evento");
        datePicker.setPlaceholder("Fecha de Evento");
        datePicker.setValue(LocalDate.now());
        Button save = new Button("Guardar");
        save.getElement().setAttribute("theme", "success");
        Button cancel = new Button("Cancelar");
        cancel.getElement().setAttribute("theme", "error");
        HorizontalLayout actions = new HorizontalLayout(save, cancel);
        actions.setSpacing(true);
        formLayout.add(title, datePicker);
        add(header, formLayout, actions);

        save.addClickListener((e)-> {
                    Event event = new Event(
                            (long)eventService.eventList().size()+1,
                            title.getValue(),
                            Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            getRandomColor()
                            );
                    try{
                        eventService.createEvent(event);
                        title.setValue("");
                        datePicker.setValue(LocalDate.now());
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    general.calendar.refresh();
                }
                );
        cancel.addClickListener( (e)->{
                    title.setValue("");
                    datePicker.setValue(LocalDate.now());
                }
        );
    }

    private CalendarItemTheme getRandomColor() {
        double num = Math.random()*8;
        if(num==0){
            return CalendarItemTheme.Black;
        }else if(num == 1) {
            return CalendarItemTheme.LightGreen;
        }else if(num==2){
            return CalendarItemTheme.LightBlue;
        }else if(num==3){
            return CalendarItemTheme.Red;
        } else if(num==4){
            return CalendarItemTheme.Green;
        } else if(num==5){
            return CalendarItemTheme.Gray;
        } else if(num==6){
            return CalendarItemTheme.Blue;
        } else if(num==7){
            return CalendarItemTheme.LightRed;
        }
        return CalendarItemTheme.Blue;
    }
}
