// src/main/java/org/example/factory/PdfDocumentFactory.java
package org.example.factory;

import org.example.document.Document;
import org.example.document.PdfDocument;

public class PdfDocumentFactory implements DocumentFactory {

    @Override
    public Document createDocument() {
        return new PdfDocument();
    }
}
