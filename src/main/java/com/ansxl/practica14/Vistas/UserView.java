package com.ansxl.practica14.Vistas;

import com.ansxl.practica14.Modelos.User;
import com.ansxl.practica14.Servicios.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;


@Route("users")
@PageTitle("Usuarios")
@SpringComponent
@UIScope
public class UserView extends VerticalLayout {
    public UserView(@Autowired UserService userService) {
        User user;

        VerticalLayout verticalLayout;

        VerticalLayout userInfo;

        HorizontalLayout actions;
        actions = new HorizontalLayout();
        Button general = new Button("Volver al inicio");
        Button logout = new Button("Salir");
        logout.getElement().setAttribute("theme", "error");
        logout.addClickListener((evento) -> {
            try {
                User u2 = userService.userList().get(0);
                u2.setLoggedIn(false);
                userService.editUser(u2);
            }  catch (Exception e) {
                e.printStackTrace();
            }
            getUI().get().navigate("");
        });
        general.addClickListener((evento) -> getUI().get().navigate("main"));
        actions.add(general, logout);


        if (userService.userList().isEmpty())
            getUI().get().navigate("");
        else if (!userService.userList().get(0).isLoggedIn())
            getUI().get().navigate("");
        else {
            user = userService.userList().get(0);

            verticalLayout = new VerticalLayout();


            setAlignItems(Alignment.CENTER);

            verticalLayout.setSpacing(true);
            verticalLayout.setAlignItems(Alignment.CENTER);


            H3 title = new H3("Práctica 14 en IT");
            H5 screen = new H5("Modificar Datos del usuario");

            VerticalLayout userData = new VerticalLayout();
            H3 name = new H3("Nombre del usuario: " + user.getName());
            H5 email = new H5("Correo electrónico: " + user.getEmail());
            userData.setAlignItems(Alignment.CENTER);
            userData.add(name, email);



            userInfo = new VerticalLayout();

            H3 titulo2 = new H3("Editar datos del usuario");
            TextField new_email = new TextField("Email");
            TextField new_name = new TextField("Nombre");
            Button guardar = new Button("Editar");
            userInfo.setAlignItems(Alignment.CENTER);

            userInfo.add(titulo2, new_name, new_email, guardar);

            verticalLayout.add(userData, userInfo);
            verticalLayout.setAlignItems(Alignment.CENTER);
            guardar.addClickListener((evento) -> {
                try {
                    if (!new_email.getValue().equals(""))
                        user.setEmail(new_email.getValue());
                    if (!new_name.getValue().equals(""))
                        user.setName(new_name.getValue());
                    userService.editUser(user);
                    getUI().get().getPage().reload();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            verticalLayout.setAlignItems(Alignment.CENTER);

            add(title, screen, actions, verticalLayout);
        }
    }
}

