package com.ansxl.practica14.Vistas;

import com.ansxl.practica14.Modelos.User;
import com.ansxl.practica14.Servicios.UserService;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@Route("gerentes")
@PageTitle("Gerentes")
@SpringComponent
@UIScope
public class Gerentes extends VerticalLayout {
    boolean updating;
    Integer originalId;

    DataProvider<User, Void> userVoidDataProvider;
    public Gerentes(@Autowired UserService userService){
        TextField name = new TextField("Nombre");
        TextField email = new TextField("Correo electrónico");
        PasswordField passwordField = new PasswordField("Contraseña");

        userVoidDataProvider = DataProvider.fromCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    return userService.userListPagination(offset,limit).stream();
                },
                query -> userService.countUsers()-1
        );

        Binder<User> userBinder = new Binder<>();
        Grid<User> userGrid = new Grid<>();

        Button add=new Button("Agregar gerente");
        add.getElement().setAttribute("theme", "success");
        Button cancel = new Button("Cancelar");
        cancel.getElement().setAttribute("theme", "error");



        if (userService.userList().isEmpty())
            getUI().get().navigate("");
        else if (!userService.userList().get(0).isLoggedIn())
            getUI().get().navigate("");
        else {
            add.addClickListener((e) -> {
                User user;
                try {
                    if (updating) {
                       user = userService.createUser(originalId, name.getValue(), email.getValue(), passwordField.getValue());
                       userVoidDataProvider.refreshItem(user);
                    } else {
                       user = userService.createUser(userService.countUsers()+1, name.getValue(), email.getValue(), passwordField.getValue());
                       userVoidDataProvider.refreshItem(user);
                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                }

                name.setValue("");
                email.setValue("");
                passwordField.setValue("");
                userVoidDataProvider.refreshAll();
            });

            cancel.addClickListener((evento) -> {
                name.setValue("");
                email.setValue("");
                passwordField.setValue("");
            });


            H4 titulo = new H4("Práctica #14 - OCJ");
            H6 subtitulo = new H6("CRUD de Gerentes");

            HorizontalLayout actions = new HorizontalLayout();

            Button general = new Button("Volver al inicio");

            Button logout = new Button("Salir");
            logout.getElement().setAttribute("theme", "error");
            general.addClickListener((evento) -> getUI().get().navigate("main"));
            actions.add(general, logout);

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

            HorizontalLayout GerenteActions = new HorizontalLayout(add, cancel);
            GerenteActions.setSpacing(true);

            name.setTitle("Nombre: ");
            email.setTitle("Email: ");
            passwordField.setTitle("Contraseña: ");

            userGrid.setDataProvider(userVoidDataProvider);
            userGrid.addColumn(User::getName).setHeader("Nombre");
            userGrid.addColumn(User::getEmail).setHeader("Email");

            userGrid.addSelectionListener(event -> {
                if (event.getFirstSelectedItem().isPresent()) {
                    Dialog dialog = new Dialog();
                    Button edit = new Button("Modificar");
                    Button delete = new Button("Eliminar");
                    edit.getElement().setAttribute("theme", "success");
                    delete.getElement().setAttribute("theme", "error");

                    HorizontalLayout actions2 = new HorizontalLayout(edit, delete);
                    actions2.setSpacing(true);
                    dialog.add(actions2);
                    delete.addClickListener((evento) -> {
                        User user = event.getFirstSelectedItem().get();
                        userService.deleteUser( user.getId());
                        userBinder.readBean(user);
                        userVoidDataProvider.refreshAll();
                    });

                    edit.addClickListener((evento) -> {
                        try {
                            User user = event.getFirstSelectedItem().get();
                            name.setValue(user.getName());
                            email.setValue(user.getEmail());
                            passwordField.setValue(user.getPassword());
                            updating = true;
                            originalId = user.getId();
                            userBinder.writeBean(user);
                            userVoidDataProvider.refreshAll();
                        } catch (ValidationException e) {
                            e.printStackTrace();
                        }
                    });
                    dialog.open();
                }
            });

            setAlignItems(Alignment.CENTER);
            FormLayout form = new FormLayout(name, email, passwordField);

            add(titulo, subtitulo, actions, form, GerenteActions, userGrid);

            name.setValue("");
            email.setValue("");
            passwordField.setValue("");
        }


    }
}
