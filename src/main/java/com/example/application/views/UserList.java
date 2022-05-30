package com.example.application.views;


import com.example.application.backend.UserService;
import com.example.application.entity.UserModel;
import com.google.cloud.storage.Blob;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import javax.annotation.security.RolesAllowed;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Sijan
 */
@RolesAllowed({"ADMIN"})
@PageTitle("User")
@Route(value = "users", layout = MainLayout.class)
@RouteAlias(value = "Users", layout = MainLayout.class)
public class UserList extends VerticalLayout {

    private final UserForm form;
    Grid<UserModel> grid = new Grid<>(UserModel.class);
    TextField filterText = new TextField();

    public UserList() {

        addClassName("list-view");
        setSizeFull();
        configureGrid();
        form = new UserForm();
        form.addListener(UserForm.SaveEvent.class, e -> {
            //validate
            if (e.getUser().validateUser()) {
                try {
                    UserModel user = e.getUser();
                    UserModel userWithURL = null;
                    if ((userWithURL = addImageURL(user)) != null) {
                        UserService.addUser(userWithURL);  // save to database with image
                    }
                    UserService.addUser(user);  // save to database without image
                    updateList();
                    closeEditor();
                } catch (IOException ex) {
                    Logger.getLogger(UserList.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                Notification notification = new Notification();
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                Div text = new Div(new Text("Please fill all the fields"));
                Button closeButton = new Button(new Icon("lumo", "cross"));
                closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
                closeButton.getElement().setAttribute("aria-label", "Close");
                closeButton.addClickListener(e1 -> notification.close());
                HorizontalLayout layout = new HorizontalLayout(text, closeButton);
                layout.setAlignItems(Alignment.CENTER);
                notification.add(layout);
                notification.open();
                notification.setDuration(3000);
                notification.setPosition(Notification.Position.MIDDLE);
            }
        });
        form.addListener(UserForm.DeleteEvent.class, e -> {
            UserService.deleteUser(e.getUser().getUid());
            updateList();
            closeEditor();
        });
        form.addListener(UserForm.CloseEvent.class, e -> closeEditor());
        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();
        this.setAlignItems(FlexComponent.Alignment.CENTER);
        add(getToolBar(), content);
        updateList();
        closeEditor();
    }

    private static TemplateRenderer<UserModel> createPersonRenderer() {
        return TemplateRenderer.<UserModel>of(
                        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                + "  <vaadin-avatar style=\"height: var(--lumo-size-m)\" img=\"[[item.imageUrl]]\" name=\"[[item.fullName]]\" alt=\"User avatar\"></vaadin-avatar>"
                                + "  <span> [[item.name]] </span>"
                                + "</vaadin-horizontal-layout>")
                .withProperty("imageUrl", UserModel::getImageUrl)
                .withProperty("name", UserModel::getName);
    }

    private UserModel addImageURL(UserModel user) throws IOException {
        InputStream inputStream = null;

        if ((inputStream = form.uploadImageToFile.loadFile()) != null) {
            byte[] bufferedImage = resizeImage(ImageIO.read(inputStream), 190, 190);
            StorageClient storageClient = StorageClient.getInstance(FirebaseApp.getInstance());
            String blobString = "hamroshareusers/" + user.getUsername() + user.getUid() + ".png";
            Blob blob = storageClient.bucket().create(blobString, bufferedImage);
            String url = blob.signUrl(999999, TimeUnit.DAYS).toString();
            user.setImageUrl(url);
        }
        return user;
    }

    private byte[] resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "png", output);

        return output.toByteArray();
    }

    private void configureGrid() {
        grid.addColumn(createPersonRenderer()).setHeader("Name").setFrozen(true)
                .setAutoWidth(true).setFlexGrow(0);
        grid.removeColumnByKey("uid");
        grid.removeColumnByKey("imageUrl");
        grid.removeColumnByKey("password");
        grid.removeColumnByKey("roles");
        grid.removeColumnByKey("name");

        grid.addClassName("user-grid");
        grid.setSizeFull();
        grid.setColumnReorderingAllowed(true);
        grid.setColumnOrder(
                grid.getColumns().get(3),
                grid.getColumns().get(2),
                grid.getColumns().get(0),
                grid.getColumns().get(1)
        );
        //grid.setColumns("name", "username", "emailID", "status");
        // grid.addColumn(col.getRenderer());
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> {
            editContact(e.getValue());
        });
    }

    private void updateList() {
        grid.setItems(UserService.findAll(filterText.getValue()));
    }

    private void closeEditor() {
        form.setUserModel(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private HorizontalLayout getToolBar() {
        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.addValueChangeListener(e -> updateList());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        Button addContactButton = new Button("Add User", e -> addUser());
        addContactButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.setWidthFull();
        toolbar.setVerticalComponentAlignment(FlexComponent.Alignment.END, addContactButton);
        toolbar.setAlignSelf(FlexComponent.Alignment.CENTER, filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addUser() {
        grid.asSingleSelect().clear();
        editContact(new UserModel());
    }

    private void editContact(UserModel userModel) {
        if (userModel == null) {
            closeEditor();
        } else {
            form.setUserModel(userModel);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}
