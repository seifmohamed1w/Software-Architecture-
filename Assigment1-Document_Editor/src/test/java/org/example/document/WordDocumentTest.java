// src/test/java/org/example/document/WordDocumentTest.java
package org.example.document;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class WordDocumentTest {

    @TempDir
    Path tempDir;

    @Test
    void save_shouldCreateDocx_andContainContent() throws Exception {
        WordDocument doc = new WordDocument();
        doc.setContent("line1\nline2");
        assertTrue(doc.getContent().contains("line1"));

        Path out = tempDir.resolve("doc"); // no extension on purpose
        doc.save(out.toString());

        Path saved = tempDir.resolve("doc.docx");
        assertTrue(Files.exists(saved));
        assertTrue(Files.size(saved) > 0);

        try (FileInputStream fis = new FileInputStream(saved.toFile());
             XWPFDocument read = new XWPFDocument(fis)) {

            String extracted = read.getParagraphs().stream()
                    .map(p -> p.getText())
                    .reduce("", (a, b) -> a + "\n" + b);

            assertTrue(extracted.contains("line1"));
            assertTrue(extracted.contains("line2"));
        }
    }

    @Test
    void save_shouldThrowOnEmptyPath() {
        WordDocument doc = new WordDocument();
        assertThrows(IllegalArgumentException.class, () -> doc.save(""));
        assertThrows(IllegalArgumentException.class, () -> doc.save("   "));
    }
}
