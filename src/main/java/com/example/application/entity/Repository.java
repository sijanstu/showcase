package com.example.application.entity;

import com.example.application.Application;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.vaadin.flow.component.html.Image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.kohsuke.github.GHRepository;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Repository {
    private String name;
    private String imageUrl;
    private String description;
    private String language;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void addRepository() {
        Application.firestore.collection("repositories").document(String.valueOf(name)).set(this);
    }

    public boolean RepositoryFromFirebase() {
        try {
            List<QueryDocumentSnapshot> documents = Application.firestore.collection("repositories").whereEqualTo("name", name).get().get().getDocuments();
            if (!documents.isEmpty()) {
                if (documents.get(0).exists()) {
                    Repository repository = documents.get(0).toObject(Repository.class);
                    this.imageUrl = repository.imageUrl;
                    return true;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    void OgRepo(String login, String repo) throws IOException {
        Document document = Jsoup.connect("https://github.com/"+login+"/"+repo).get();
        setName(repo);
        setUrl("https://github.com/"+login+"/"+repo);
        setImageUrl(document.select("meta[property=og:image]").attr("content"));
        setDescription(document.select("meta[property=og:description]").attr("content"));
        setLanguage(document.select("meta[property=og:language]").attr("content"));
    }
}
