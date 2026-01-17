// src/main/java/org/example/factory/WordDocumentFactory.java
package org.example.factory;

import org.example.document.Document;
import org.example.document.WordDocument;

public class WordDocumentFactory implements DocumentFactory {

    @Override
    public Document createDocument() {
        return new WordDocument();
    }
}
