// src/main/java/org/example/Main.java
package org.example;

import org.example.editor.DocumentEditor;
import org.example.factory.DocumentFactory;
import org.example.factory.DocumentFactoryRegistry;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Select document type (pdf | word | html): ");
            String type = scanner.nextLine();

            DocumentFactory factory = DocumentFactoryRegistry.getFactory(type);
            DocumentEditor editor = new DocumentEditor(factory);

            System.out.println("Enter document content (single line): ");
            String content = scanner.nextLine();
            editor.setContent(content);

            System.out.println();
            editor.display();

            System.out.println("Enter save path (e.g., output.pdf / output.docx / output.html): ");
            String path = scanner.nextLine();
            editor.save(path);

        } catch (IllegalArgumentException ex) {
            System.err.println("Error: " + ex.getMessage());
        } finally {
            scanner.close();
        }
    }
}
