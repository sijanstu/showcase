package com.example.application.views;

import com.example.application.backend.UserService;
import com.example.application.entity.UserModel;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.concurrent.ExecutionException;

/**
 * @author Sijan
 */
public class UserForm extends FormLayout {

    UploadImageToFile uploadImageToFile = new UploadImageToFile();
    Avatar avatar = new Avatar();
    TextField name = new TextField("Full name");
    TextField username = new TextField("User name");
    EmailField emailID = new EmailField("Email");
    PasswordField password = new PasswordField("Password");
    ComboBox<UserModel.Status> status = new ComboBox<>("Status");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    String uid = null;
    Binder<UserModel> binder = new BeanValidationBinder<>(UserModel.class);

    public UserForm() {
        uploadImageToFile.setAvatar(avatar);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(avatar, uploadImageToFile);
        horizontalLayout.setSpacing(true);
        //horizontalLayout.setWidth("100%");
        //horizontalLayout.setHeight("100%");
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        addClassName("user-form");
        binder.bindInstanceFields(this);
        status.setItems(UserModel.Status.values());
        add(horizontalLayout, name, emailID, username, password, status, createButtonsLayout());
    }

    public void setUserModel(UserModel user) {

        if (user != null) {
            if (user.getImageUrl() != null) {
                uid = user.getUid();
                avatar.setImage(user.getImageUrl());
            } else {
                uid = null;
                avatar.setImage(null);
            }
        } else {
            uid = null;
            avatar.setImage(null);
        }
        binder.removeBinding("roles");
        binder.setBean(user);
        binder.removeBinding("roles");

    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> {
            //save user
            try {
                if (uid == null) {
                    if (UserService.checkUserNameAvailability(username.getValue())) {
                        Notification notification = new Notification();
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        Div text = new Div(new Text("Username not available, try another one"));
                        Button closeButton = new Button(new Icon("lumo", "cross"));
                        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
                        closeButton.getElement().setAttribute("aria-label", "Close");
                        closeButton.addClickListener(e1 -> notification.close());
                        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
                        layout.setAlignItems(FlexComponent.Alignment.CENTER);
                        notification.add(layout);
                        notification.open();
                        notification.setDuration(3000);
                        notification.setPosition(Notification.Position.MIDDLE);
                    }
                } else {

                    fireEvent(new SaveEvent(this, binder.getBean()));
                }
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("error:" + e.getMessage());
            }
        });
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(click1 -> fireEvent(new CloseEvent(this)));
        binder.addStatusChangeListener(event -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public static abstract class ContactFormEvent extends ComponentEvent<UserForm> {

        private final UserModel user;

        protected ContactFormEvent(UserForm source, UserModel user) {
            super(source, false);
            this.user = user;
        }

        public UserModel getUser() {
            return user;
        }
    }

    public static class SaveEvent extends ContactFormEvent {

        SaveEvent(UserForm source, UserModel user) {

            super(source, user);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {

        DeleteEvent(UserForm source, UserModel user) {
            super(source, user);
        }
    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(UserForm source) {
            super(source, null);
        }
    }

}
