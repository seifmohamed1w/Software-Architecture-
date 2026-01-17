// src/main/java/org/example/factory/DocumentFactoryRegistry.java
package org.example.factory;

import java.util.HashMap;
import java.util.Map;

public final class DocumentFactoryRegistry {

    private static final Map<String, DocumentFactory> FACTORIES = new HashMap<>();

    static {
        // registration – add new formats here without touching editor code
        FACTORIES.put("pdf", new PdfDocumentFactory());
        FACTORIES.put("word", new WordDocumentFactory());
        FACTORIES.put("html", new HtmlDocumentFactory());
    }

    private DocumentFactoryRegistry() {
        // prevent instantiation
    }

    public static DocumentFactory getFactory(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Document type cannot be null");
        }

        DocumentFactory factory = FACTORIES.get(type.trim().toLowerCase());

        if (factory == null) {
            throw new IllegalArgumentException(
                    "Unsupported document type: " + type +
                            ". Supported types: " + FACTORIES.keySet()
            );
        }

        return factory;
    }
}
