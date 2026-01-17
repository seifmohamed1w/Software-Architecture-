// src/test/java/org/example/document/HtmlDocumentTest.java
package org.example.document;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class HtmlDocumentTest {

    @TempDir
    Path tempDir;

    @Test
    void save_shouldCreateHtmlFile_andContainContent() throws Exception {
        HtmlDocument doc = new HtmlDocument();
        doc.setContent("hello");
        assertEquals("hello", doc.getContent());

        Path out = tempDir.resolve("page"); // no extension on purpose
        doc.save(out.toString());

        Path saved = tempDir.resolve("page.html");
        assertTrue(Files.exists(saved));
        assertTrue(Files.size(saved) > 0);

        String text = Files.readString(saved);
        assertTrue(text.contains("hello"));
        assertTrue(text.toLowerCase().contains("<html"));
    }

    @Test
    void save_shouldThrowOnEmptyPath() {
        HtmlDocument doc = new HtmlDocument();
        assertThrows(IllegalArgumentException.class, () -> doc.save("   "));
    }
}
