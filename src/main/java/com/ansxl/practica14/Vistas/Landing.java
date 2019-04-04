package com.ansxl.practica14.Vistas;

import com.ansxl.practica14.Modelos.User;
import com.ansxl.practica14.Servicios.UserService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
@SpringComponent
@UIScope
public class Landing extends VerticalLayout {
    public Landing(@Autowired UserService userService) {
        TextField name = new TextField("Nombre Completo");
        TextField email = new TextField("Correo eléctronico");
        PasswordField password = new PasswordField("Contraseña");

        boolean empty = userService.userList().isEmpty();

        Button loginOrSignUp = empty? new Button("Crear Nuevo Usuario") : new Button("Iniciar Sesión");
        loginOrSignUp.getElement().setAttribute("theme", "primary");
        loginOrSignUp.setIcon(new Icon(empty?VaadinIcon.SIGN_IN_ALT:VaadinIcon.SIGN_IN));
        H3 title = new H3("Práctica 14 en IT");
        H5 subtitle = empty ? new H5("Crear cuenta") : new H5("Iniciar Sesión");

        setAlignItems(Alignment.CENTER);
        loginOrSignUp.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            if(empty){
                try {
                    userService.createUser(userService.countUsers()+1, name.getValue(), email.getValue(), password.getValue());
                    getUI().get().getPage().reload();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }else{
                if(userService.userExists(email.getValue(), password.getValue())){
                    try{
                        User user = userService.userList().get(0);
                        user.setLoggedIn(true);
                        userService.editUser(user);
                        getUI().get().navigate("main");
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                } else {
                    getUI().get().getPage().reload();
                }
            }
        });
        VerticalLayout verticalLayout;
        verticalLayout = empty? new VerticalLayout(name,email,password, loginOrSignUp): new VerticalLayout(email,password, loginOrSignUp);

        setAlignItems(Alignment.CENTER);
        verticalLayout.setWidth("300px");
        add(title,subtitle, verticalLayout);



    }
}
