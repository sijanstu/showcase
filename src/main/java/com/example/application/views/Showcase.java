package com.example.application.views;

import com.example.application.entity.GitUser;
import com.example.application.entity.Repository;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.List;

@PageTitle("Project Showcase")
@Route(value = "showcase", layout = MainLayout.class)
@RouteAlias(value = "showcase", layout = MainLayout.class)
@AnonymousAllowed
public class Showcase extends FormLayout {
    GHRepository repository;
    private OrderedList imageContainer;

    public Showcase() {

        try {
            GitHub github = GitHub.connect("github username", "your github key");
            repository = github.getRepository("github username/a public repo name");
            GitUser gitUser = GitUser.FromFirebase("github username");
            if (gitUser == null) {
                gitUser= getGitUser("github username","a public repo name");
            }
            List<Repository> repositories = gitUser.getPublic_repos();
            for (Repository repository : repositories) {
                Div card = new Div();
                card.addClassNames("border-5", "border-dashed", "border-gray-200", "rounded-lg", "shadow-lg", "w-full");
                card.add(new ImageListViewCard(repository));
                add(card);
            }
            addClassNames("items-center", "justify-between");

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void constructUI() {
        addClassNames("image-list-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");
        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames("items-center", "justify-between");
        VerticalLayout headerContainer = new VerticalLayout();
        H2 header = new H2("Popular Repositories");
        header.addClassNames("mb-0", "mt-xl", "text-3xl");
        Paragraph description = null;
        try {
            description = new Paragraph(repository.getOwner().getBio());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        description.addClassNames("mb-xl", "mt-0", "text-secondary");
        headerContainer.add(header, description);

        Select<String> sortBy = new Select<>();
        sortBy.setLabel("Sort by");
        sortBy.setItems("Popularity", "Newest first", "Oldest first");
        sortBy.setValue("Popularity");
        imageContainer = new OrderedList();
        imageContainer.addClassNames("gap-m", "grid", "list-none", "m-0", "p-0");
        container.add(headerContainer, sortBy, imageContainer);
        add(container);
    }

    GitUser getGitUser(String username,String repo) throws IOException {
        GitUser gitUser = new GitUser();
        GHRepository repository = GitHub.connectAnonymously().getRepository(username+"/"+repo);
        gitUser.setAvatar_url(repository.getOwner().getAvatarUrl());
        gitUser.setBio(repository.getOwner().getBio());
        gitUser.setCompany(repository.getOwner().getCompany());
        gitUser.setEmail(repository.getOwner().getEmail());
        gitUser.setFollowers(String.valueOf(repository.getOwner().getFollowers()));
        gitUser.setFollowing(String.valueOf(repository.getOwner().getFollows()));
        gitUser.setHtml_url(repository.getOwner().getHtmlUrl().toString());
        gitUser.setLocation(repository.getOwner().getLocation());
        gitUser.setName(username);
        gitUser.setLogin(username);
        gitUser.setPublic_repos(gitUser.repos(gitUser.getName()));
        gitUser.addRepository();
        return gitUser;
    }

}