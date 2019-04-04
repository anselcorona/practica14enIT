package com.ansxl.practica14.Vistas;

import com.sendgrid.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
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
        send.getElement().setAttribute("theme", "success");
        Button cancel = new Button("Cancelar");
        cancel.getElement().setAttribute("theme", "error");
        HorizontalLayout actions = new HorizontalLayout(send, cancel);
        actions.setSpacing(true);
        formLayout.add(To, Subject, Content);
        setAlignItems(Alignment.CENTER);
        add(title, formLayout, actions);
        send.addClickListener((e)->{
            com.sendgrid.Email from = new com.sendgrid.Email("ansxldo@gmail.com");
            String subject = Subject.getValue();
            com.sendgrid.Email to = new com.sendgrid.Email(To.getValue());
            com.sendgrid.Content content = new Content("text/plain", Content.getValue());
            Mail email = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
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
