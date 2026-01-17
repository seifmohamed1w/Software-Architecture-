package org.example.document;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PdfDocumentTest {

    @TempDir
    Path tempDir;

    @Test
    void save_shouldCreatePdf_andContainContent() throws Exception {
        PdfDocument doc = new PdfDocument();
        doc.setContent("pdf line 1\npdf line 2");

        Path out = tempDir.resolve("file"); // no extension on purpose
        doc.save(out.toString());

        Path saved = tempDir.resolve("file.pdf");
        assertTrue(Files.exists(saved));
        assertTrue(Files.size(saved) > 0);

        try (PDDocument pdf = Loader.loadPDF(saved.toFile())) {
            String extracted = new PDFTextStripper().getText(pdf);
            assertTrue(extracted.contains("pdf line 1"));
            assertTrue(extracted.contains("pdf line 2"));
        }
    }

    @Test
    void save_shouldThrowOnEmptyPath() {
        PdfDocument doc = new PdfDocument();
        assertThrows(IllegalArgumentException.class, () -> doc.save(" "));
        assertThrows(IllegalArgumentException.class, () -> doc.save(""));
    }
}
