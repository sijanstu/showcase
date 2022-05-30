package com.example.application.views;

import com.example.application.entity.AskBuddieRepository;
import com.example.application.entity.Repository;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@PageTitle("Project Showcase")
@Route(value = "askshowcase", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class AskShowcase extends FormLayout {
    public AskShowcase() throws IOException {
        addClassNames("items-center", "justify-between");
        List<AskBuddieRepository> askBuddieRepositories = AskBuddieRepository.getAllRepositories();

        if (askBuddieRepositories.size() > 0) {
            for (AskBuddieRepository askBuddieRepository : askBuddieRepositories) {
                Div card = new Div();
                card.addClassNames("border-5", "border-dashed", "border-gray-200", "rounded-lg", "shadow-lg", "w-full");
                Repository repository = new Repository();
                repository.setName(askBuddieRepository.getTitle()+" "+askBuddieRepository.getTags());
                repository.setDescription(askBuddieRepository.getExcerpt());
                repository.setUrl(askBuddieRepository.getProjectLink());
                repository.setLanguage(askBuddieRepository.getTags().toString());
                repository.setImageUrl(askBuddieRepository.getFeaturedImg());
                card.add(new ImageListViewCard(repository));
                add(card);
            }

        } else {
            String ShowCaseLink = "https://github.com/askbuddie/showcase/tree/main/src/content";
            Document document = Jsoup.parse(new URL(ShowCaseLink), 20000);
            Elements links = document.getElementsByClass("js-navigation-open Link--primary");
            links.forEach(link -> {
                try {
                    Div card = new Div();
                    card.addClassNames("border-5", "border-dashed", "border-gray-200", "rounded-lg", "shadow-lg", "w-full");
                    Repository repository = new Repository();
                    String linkUrl = "https://github.com/askbuddie/showcase/blob/main/src/content/"+link.text();
                    Document repoDocument = Jsoup.parse(new URL(linkUrl), 20000);
                    Element dataTable=Objects.requireNonNull(repoDocument.getElementById("readme")).getElementsByTag("table").get(0);
                    repository.setName(dataTable.getElementsByTag("td").get(0).text());
                    repository.setDescription(dataTable.getElementsByTag("td").get(1).text());
                    repository.setLanguage(dataTable.getElementsByTag("td").get(2).text());
                    repository.setUrl(dataTable.getElementsByTag("td").get(4).getElementsByTag("a").get(0).attr("href"));
                    String imageUrl=dataTable.getElementsByTag("td").get(3).text();
                    repository.setImageUrl("https://raw.githubusercontent.com/askbuddie/showcase/main/src"+imageUrl.replace("../","/"));
                    try {
                        int status=Jsoup.connect(repository.getImageUrl()).followRedirects(false).ignoreContentType(true).execute().statusCode();
                        if(status!=200){
                            Document imgDocument = Jsoup.connect(repository.getUrl()).get();
                            repository.setImageUrl(imgDocument.select("meta[property=og:image]").attr("content"));
                        }
                    }catch (Exception e){
                        Document imgDocument = Jsoup.connect(repository.getUrl()).get();
                        repository.setImageUrl(imgDocument.select("meta[property=og:image]").attr("content"));
                    }

                    card.add(new ImageListViewCard(repository));
                    add(card);
                    AskBuddieRepository askBuddieRepository = new AskBuddieRepository();
                    askBuddieRepository.setTitle(repository.getName());
                    askBuddieRepository.setExcerpt(repository.getDescription());
                    askBuddieRepository.setProjectLink(repository.getUrl());
                    askBuddieRepository.setTags(new ArrayList<>(List.of(repository.getLanguage())));
                    askBuddieRepository.setFeaturedImg(repository.getImageUrl());
                    askBuddieRepository.addRepositoryToFirebase();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }
}
