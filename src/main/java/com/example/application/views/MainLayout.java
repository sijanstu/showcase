package com.example.application.views;


import com.example.application.backend.UserService;
import com.example.application.entity.UserModel;
import com.example.application.login.LoginView;
import com.example.application.security.AuthenticatedUser;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Meta;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    public MainLayout() {
        UI.getCurrent().getElement().setAttribute("theme", Lumo.DARK);
        addToNavbar(createHeaderContent());
    }

    private Component createHeaderContent() {
        Header header = new Header();
        header.addClassNames("bg-base", "border-b", "border-contrast-10", "box-border", "flex", "flex-col", "w-full");

        Div layout = new Div();
        layout.addClassNames("flex", "h-xl", "items-center", "px-l");

        H1 appName = new H1("Buddie ShowCase");
        appName.addClassNames("my-0", "me-auto", "text-l");
        layout.add(appName);

        Nav nav = new Nav();
        nav.addClassNames("flex", "gap-s", "overflow-auto", "px-m");

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        list.addClassNames("flex", "list-none", "m-0", "p-0");
        nav.add(list);

        for (MenuItemInfo menuItem : createMenuItems()) {
            list.add(menuItem);

        }
        if (AuthenticatedUser.getUser()) {
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            Button logout = new Button("Logout");
            if (AuthenticatedUser.getUser()) {
                if (AuthenticatedUser.get().isPresent()) {
                    Avatar avatar = new Avatar();
                    avatar.setImage(AuthenticatedUser.get().get().getImageUrl());
                    logout.addClickListener(event -> {

                        UI.getCurrent().getSession().close();
                        new AuthenticatedUser(new UserService()).logout();
                    });

                    horizontalLayout.add(logout, avatar);
                    horizontalLayout.addClassNames("flex", "items-center", "gap-m");
                    list.add(horizontalLayout);
                }
            }
        } else {
            Button login = new Button("Login");
            login.addClickListener(event ->
                    UI.getCurrent().navigate(LoginView.class));
            list.add(login);
        }

        header.add(layout, nav);
        return header;
    }

    private MenuItemInfo[] createMenuItems() {
        if (AuthenticatedUser.get().isPresent()) {
            if (AuthenticatedUser.get().get().getStatus().compareTo(UserModel.Status.ADMIN) == 0) {
                return new MenuItemInfo[]{ //
                        new MenuItemInfo("AskBuddie ShowCase", "la la-home", AskShowcase.class), //
                        new MenuItemInfo("My ShowCase", "la la-home", Showcase.class),
                        new MenuItemInfo("Manage Users", "la la-users", UserList.class),//
                        new MenuItemInfo("About", "la la-info-circle\n", AboutView.class),
                };
            } else {
                return new MenuItemInfo[]{ //
                        new MenuItemInfo("AskBuddie ShowCase", "la la-home", AskShowcase.class),
                        new MenuItemInfo("My ShowCase", "la la-home", Showcase.class),
                        new MenuItemInfo("About", "la la-info-circle\n", AboutView.class),
                };
            }

        } else {
            return new MenuItemInfo[]{ //
                    new MenuItemInfo("AskBuddie ShowCase", "la la-home", AskShowcase.class),
                    new MenuItemInfo("My ShowCase", "la la-home", Showcase.class),
                    new MenuItemInfo("About", "la la-info-circle\n", AboutView.class),
            };

        }

    }

    /**
     * A simple navigation item component, based on ListItem element.
     */
    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, String iconClass, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            // Use Lumo classnames for various styling
            link.addClassNames("flex", "h-m", "items-center", "px-s", "relative", "text-secondary");
            link.setRoute(view);

            Span text = new Span(menuTitle);
            // Use Lumo classnames for various styling
            text.addClassNames("font-medium", "text-s", "whitespace-nowrap");

            link.add(new LineAwesomeIcon(iconClass), text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }

        /**
         * Simple wrapper to create icons using LineAwesome iconset. See
         * https://icons8.com/line-awesome
         */
        @NpmPackage(value = "line-awesome", version = "1.3.0")
        public static class LineAwesomeIcon extends Span {
            public LineAwesomeIcon(String lineawesomeClassNames) {
                // Use Lumo class names for suitable font size and margin
                addClassNames("me-s", "text-l");
                if (!lineawesomeClassNames.isEmpty()) {
                    addClassNames(lineawesomeClassNames);
                }
            }
        }

    }

}
