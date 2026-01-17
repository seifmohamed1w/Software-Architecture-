package org.example.document;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;


import java.io.IOException;
import java.nio.file.Path;

public class PdfDocument implements Document {

    private String content = "";

    @Override
    public void setContent(String text) {
        this.content = (text == null) ? "" : text;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void display() {
        System.out.println("=== PDF DOCUMENT (Preview) ===");
        System.out.println(content);
        System.out.println("==============================");
    }

    @Override
    public void save(String path) {
        Path out = ensureExtension(path, ".pdf").toAbsolutePath();

        try (PDDocument pdf = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            pdf.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(pdf, page)) {
                cs.beginText();
                cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);


                // Basic margins
                float marginLeft = 50;
                float startY = page.getMediaBox().getHeight() - 60;
                cs.newLineAtOffset(marginLeft, startY);

                float leading = 14.5f;

                // Write lines; auto-wrap is not implemented (simple assignment version)
                String[] lines = content.split("\\R", -1);
                for (String line : lines) {
                    cs.showText(sanitizePdfText(line));
                    cs.newLineAtOffset(0, -leading);
                }

                cs.endText();
            }

            pdf.save(out.toFile());
            System.out.println("Saved PDF document to: " + out);

        } catch (IOException e) {
            throw new RuntimeException("Failed to save PDF to: " + out, e);
        }
    }

    private String sanitizePdfText(String s) {
        // PDFBox showText does not accept some control chars
        return (s == null) ? "" : s.replace("\t", "    ");
    }

    private Path ensureExtension(String path, String ext) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Path cannot be empty.");
        }
        String p = path.trim();
        if (!p.toLowerCase().endsWith(ext)) {
            p += ext;
        }
        return Path.of(p);
    }
}
