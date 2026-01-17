package org.example.document;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class HtmlDocument implements Document {

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
        System.out.println("=== HTML DOCUMENT (Preview) ===");
        System.out.println(renderHtml());
        System.out.println("================================");
    }

    @Override
    public void save(String path) {
        Path out = ensureExtension(path, ".html").toAbsolutePath();
        try {
            Files.writeString(out, renderHtml(), StandardCharsets.UTF_8);
            System.out.println("Saved HTML document to: " + out);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save HTML to: " + out, e);
        }
    }

    private String renderHtml() {
        // Minimal wrapper; if you want strict HTML escaping, add it later.
        return "<!doctype html>\n<html>\n  <head>\n    <meta charset=\"utf-8\" />\n    <title>Document</title>\n  </head>\n"
                + "  <body>\n    <pre>\n"
                + content
                + "\n    </pre>\n  </body>\n</html>\n";
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
