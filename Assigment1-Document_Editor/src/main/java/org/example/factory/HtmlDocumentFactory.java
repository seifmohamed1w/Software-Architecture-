// src/main/java/org/example/factory/HtmlDocumentFactory.java
package org.example.factory;

import org.example.document.Document;
import org.example.document.HtmlDocument;

public class HtmlDocumentFactory implements DocumentFactory {

    @Override
    public Document createDocument() {
        return new HtmlDocument();
    }
}
