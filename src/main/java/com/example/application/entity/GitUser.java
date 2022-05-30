package com.example.application.entity;

import com.example.application.Application;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GitUser {
    private String login;
    private String avatar_url;
    private String html_url;
    private String name;
    private String company;
    private String blog;
    private String location;
    private String email;
    private String bio;
    private List<Repository> public_repos;
    private String followers;
    private String following;

    static public GitUser FromFirebase(String login) {
        GitUser gitUser = new GitUser();
        try {
            List<QueryDocumentSnapshot> documents = Application.firestore.collection("githubusers").whereEqualTo("login", login).get().get().getDocuments();
            if (!documents.isEmpty()) {
                if (documents.get(0).exists()) {
                    gitUser = documents.get(0).toObject(GitUser.class);
                    return gitUser;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Repository> getPublic_repos() {
        return public_repos;
    }

    public void setPublic_repos(List<Repository> public_repos) {
        this.public_repos = public_repos;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public void addRepository() {
        Application.firestore.collection("githubusers").document(String.valueOf(login)).set(this);
    }

   static public List<Repository> repos(String name) throws IOException {

        String purl = "https://github.com/" + name + "?tab=repositories";
        Document doc = Jsoup.parse(new URL(purl), 20000);
        Elements repos = doc.getElementsByAttributeValue("itemprop", "name codeRepository");
        ArrayList<String> repoNames = new ArrayList<>();
        repos.forEach(repo -> {
            repoNames.add(repo.text());
        });
        List<Repository> reposArray = new ArrayList<>();
        repoNames.forEach(repoName -> {
            Repository repository = new Repository();
            try {
                repository.OgRepo(name, repoName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            reposArray.add(repository);

        });
        return reposArray;
    }
}
