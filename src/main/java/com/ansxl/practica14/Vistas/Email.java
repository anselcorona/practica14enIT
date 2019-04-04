package com.ansxl.practica14.Vistas;

import com.sendgrid.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.io.IOException;

@SpringComponent
@UIScope
public class Email extends VerticalLayout {
    TextField To = new TextField("Destinatario:");
    TextField Subject = new TextField("Asunto:");
    TextArea Content = new TextArea("Contenido:");

    public Email(){
        FormLayout formLayout = new FormLayout();

        H2 title = new H2("Nuevo Correo");
        Button send = new Button("Enviar");
        send.setIcon(new Icon(VaadinIcon.ARROW_FORWARD));
        send.getElement().setAttribute("theme", "success");
        Button cancel = new Button("Cancelar");
        cancel.setIcon(new Icon(VaadinIcon.STOP));
        cancel.getElement().setAttribute("theme", "error");
        HorizontalLayout actions = new HorizontalLayout(send, cancel);
        actions.setSpacing(true);
        VerticalLayout verticalLayout = new VerticalLayout(To,Subject,Content);
        verticalLayout.setWidth("1000px");
        formLayout.add(title, verticalLayout, actions);
        setAlignItems(Alignment.CENTER);
        add(formLayout);
        send.addClickListener((e)->{
            com.sendgrid.Email from = new com.sendgrid.Email("ansxldo@gmail.com");
            String subject = Subject.getValue();
            com.sendgrid.Email to = new com.sendgrid.Email(To.getValue());
            com.sendgrid.Content content = new Content("text/plain", Content.getValue());
            Mail email = new Mail(from, subject, to, content);

            String apiKey = "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";
            SendGrid sg = new SendGrid(apiKey);
            Request request = new Request();

            try {
                request.method = Method.POST;
                request.endpoint = "mail/send";
                request.body = email.build();
                Response response = sg.api(request);

                System.out.println(response.statusCode);
                System.out.println(response.body);
                System.out.println(response.headers);

                To.setValue("");
                Subject.setValue("");
                Content.setValue("");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        cancel.addClickListener((e) -> {
            To.setValue("");
            Subject.setValue("");
            Content.setValue("");
        });
    }
}
