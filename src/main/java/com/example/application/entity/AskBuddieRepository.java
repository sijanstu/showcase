package com.example.application.entity;

import com.example.application.Application;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AskBuddieRepository {
    private String title;
    private String excerpt;
    private List<String> tags;
    private String featuredImg;
    private String projectLink;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getFeaturedImg() {
        return featuredImg;
    }

    public void setFeaturedImg(String featuredImg) {
        this.featuredImg = featuredImg;
    }

    public String getProjectLink() {
        return projectLink;
    }

    public void setProjectLink(String projectLink) {
        this.projectLink = projectLink;
    }
    public void addRepositoryToFirebase() {
        Application.firestore.collection("askBuddieFeatures").document(String.valueOf(title)).set(this);
    }
    public boolean fetchRepositoryFromFirebase() {
        try {
            List<QueryDocumentSnapshot> documents = Application.firestore.collection("askBuddieFeatures").whereEqualTo("projectLink", projectLink).get().get().getDocuments();
            if (!documents.isEmpty()) {
                if (documents.get(0).exists()) {
                    AskBuddieRepository repository = documents.get(0).toObject(AskBuddieRepository.class);
                    setTitle(repository.getTitle());
                    setExcerpt(repository.getExcerpt());
                    setTags(repository.getTags());
                    setFeaturedImg(repository.getFeaturedImg());
                    setProjectLink(repository.getProjectLink());
                    return true;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public static List<AskBuddieRepository> getAllRepositories() {
        try {
            List<QueryDocumentSnapshot> documents = Application.firestore.collection("askBuddieFeatures").get().get().getDocuments();
            List<AskBuddieRepository> repositories = new ArrayList<>();
            if (!documents.isEmpty()) {
                documents.forEach(document -> {
                    repositories.add(document.toObject(AskBuddieRepository.class));
                });
            }
            return repositories;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
