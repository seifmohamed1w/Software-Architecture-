package org.example.document;

public interface Document {
    void setContent(String text);
    String getContent();

    void display();
    void save(String path);
}
