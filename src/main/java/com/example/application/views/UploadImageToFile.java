/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.application.views;

/**
 * @author Sijan
 */

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.FailedEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.StreamResource;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UploadImageToFile extends VerticalLayout {


    Upload upload;
    Avatar avatar = new Avatar();
    private File file;

    UploadImageToFile() {
        upload = new Upload(this::receiveUpload);
        Div output = new Div(new Text("Upload new Picture"));
        add(upload);

        // Configure upload component
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        upload.addSucceededListener(event -> {
            output.removeAll();
            // output.add(new Text("Uploaded: " + originalFileName + " to " + file.getAbsolutePath() + "Type: " + mimeType));
            //output.add(new Image(new StreamResource(this.originalFileName,this::loadFile),"Uploaded image"));
        });
        upload.addFailedListener((FailedEvent event) -> {
            output.removeAll();
            output.add(new Text("Upload failed: " + event.getReason()));
        });
        upload.addFileRejectedListener(event -> {
            System.out.println("File rejected: ");
        });

    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    /**
     * Load a file from local filesystem.
     *
     * @return
     */
    public InputStream loadFile() {
        try {
            if (file != null) {
                return new FileInputStream(file);
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Failed to create InputStream for: '" + this.file.getAbsolutePath(), e);
        }
        return null;
    }

    /**
     * Receive a uploaded file to a file.
     *
     * @param originalFileName
     * @param MIMEType
     * @return
     */
    public OutputStream receiveUpload(String originalFileName, String MIMEType) {
        try {
            // Create a temporary file for example, you can provide your file here.
            this.file = File.createTempFile("prefix-", "-suffix");
            avatar.setImageResource(new StreamResource(originalFileName, this::loadFile));
            System.out.println("Changed");
            file.deleteOnExit();
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Failed to create InputStream for: '" + this.file.getAbsolutePath(), e);
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Failed to create InputStream for: '" + this.file.getAbsolutePath() + "'", e);
        }

        return null;
    }
}