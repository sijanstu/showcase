package com.example.application.views;

import ch.qos.logback.core.Layout;
import com.example.application.entity.Repository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.Pane;
import com.vaadin.flow.component.charts.model.PaneList;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.io.IOException;

public class ImageListViewCard extends Div {

    public ImageListViewCard(Repository repository) throws IOException {
       addClassNames("p-2", "mb-2", "bg-light", "shadow", "text-center", "align-items-center");
        String name = repository.getName();
        Image image = new Image();
        Div div = new Div();
        div.addClassNames("flex items-center", "justify-center", "mb-m", "overflow-hidden",
                "rounded-m w-full, h-full");

        div.setHeight("230px");
        image.setHeightFull();
        if (repository.getImageUrl() != null) {
        image.setSrc(repository.getImageUrl());}
        image.setAlt(repository.getName());
        image.setAlt(name);
        image.setClassName("rounded-m w-full, h-full, object-cover, object-center, rounded-m, w-full, h-full, object-cover, object-center");
        div.add(image);
        Span header = new Span();
        header.addClassNames("text-xl", "font-semibold", "text-center", "text-gray-700");
        header.setText(name);
        header.addClickListener(event -> {
            UI.getCurrent().getPage().executeJavaScript("window.open('" + repository.getUrl() + "');");
                });

        Span subtitle = new Span();
        subtitle.addClassNames("text-sm", "font-medium", "text-center", "text-gray-500");
        Paragraph description = new Paragraph(repository.getDescription());
        description.addClassName("my-m");
        Button badge = new Button("Open");
        badge.getElement().setAttribute("theme", "badge");
        badge.addClickListener(e -> {
            UI.getCurrent().getPage().executeJavaScript("window.open('" + repository.getUrl() + "');");
        });
        image.addClickListener(e -> {
            UI.getCurrent().getPage().executeJavaScript("window.open('" + repository.getUrl() + "');");
                });
        HorizontalLayout layout1 = new HorizontalLayout();
        layout1.add(header);
        layout1.setClassName("flex items-center justify-center");
        HorizontalLayout layout2 = new HorizontalLayout();
        layout2.add(subtitle,description);
        layout2.setClassName("flex items-center justify-center");
        add(div,layout1,layout2);
    }
}
