package com.example.application.views;

import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
@RouteAlias(value = "about", layout = MainLayout.class)
@AnonymousAllowed
public class AboutView extends HorizontalLayout {
    public AboutView() {
        IFrame iFrame = new IFrame("https://sijanbhandary.com.np/");
        setSizeFull();
        iFrame.setSizeFull();
        add(iFrame);
    }

}
