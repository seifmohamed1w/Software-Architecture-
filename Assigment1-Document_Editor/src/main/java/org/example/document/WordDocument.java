package org.example.document;

import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class WordDocument implements Document {

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
        System.out.println("=== WORD DOCUMENT (Preview) ===");
        System.out.println(content);
        System.out.println("================================");
    }

    @Override
    public void save(String path) {
        Path out = ensureExtension(path, ".docx").toAbsolutePath();

        try (XWPFDocument doc = new XWPFDocument();
             FileOutputStream fos = new FileOutputStream(out.toFile())) {

            XWPFParagraph paragraph = doc.createParagraph();
            XWPFRun run = paragraph.createRun();

            // Preserve line breaks typed by the user
            String[] lines = content.split("\\R", -1);
            for (int i = 0; i < lines.length; i++) {
                run.setText(lines[i]);
                if (i < lines.length - 1) {
                    run.addBreak(BreakType.TEXT_WRAPPING);
                }
            }

            doc.write(fos);
            System.out.println("Saved Word document to: " + out);

        } catch (IOException e) {
            throw new RuntimeException("Failed to save Word (.docx) to: " + out, e);
        }
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
