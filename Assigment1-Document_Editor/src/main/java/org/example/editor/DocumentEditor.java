// src/main/java/org/example/editor/DocumentEditor.java
package org.example.editor;

import org.example.document.Document;
import org.example.factory.DocumentFactory;

import java.util.Objects;

public class DocumentEditor {

    private final Document document;

    public DocumentEditor(DocumentFactory factory) {
        Objects.requireNonNull(factory, "factory cannot be null");
        this.document = factory.createDocument();
    }

    public void setContent(String text) {
        document.setContent(text);
    }

    public String getContent() {
        return document.getContent();
    }

    public void display() {
        document.display();
    }

    public void save(String path) {
        document.save(path);
    }

    // Optional: helpful for debugging/tests
    public String getDocumentType() {
        return document.getClass().getSimpleName();
    }
}
