package com.example.application;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * The entry point of the Spring Boot application.
 * <p>
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@Theme(value = "myapp")
@PWA(name = "Sijanstu Admin Panel", shortName = "Sijanstu")
@NpmPackage(value = "line-awesome", version = "1.3.0")
@EnableWebSecurity
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

    public static Firestore firestore = null;

    public static void main(String[] args) throws IOException {
        new Application().initFirebaseSDK();
        SpringApplication.run(Application.class, args);
    }

    public void initFirebaseSDK() {
        if (FirebaseApp.getApps().isEmpty()) {
            try {

                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(getServiceAccount()))
                        .setDatabaseUrl("database url")
                        .setStorageBucket("database url")
                        .build();

                FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
                FirebaseAuth.getInstance(firebaseApp);

            } catch (IOException | URISyntaxException exc) {
                System.out.println("Firebase exception " + exc);

            }

        }
        firestore = FirestoreClient.getFirestore();
    }

    InputStream getServiceAccount() throws IOException, URISyntaxException {
        URL url = new URL("servicekey file url");
        return url.openStream();
    }

}
