// src/test/java/org/example/editor/DocumentEditorTest.java
package org.example.editor;

import org.example.document.Document;
import org.example.factory.DocumentFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentEditorTest {

    @Test
    void constructor_shouldThrowWhenFactoryNull() {
        assertThrows(NullPointerException.class, () -> new DocumentEditor(null));
    }

    @Test
    void editor_shouldDelegateToDocument() {
        FakeDocumentFactory factory = new FakeDocumentFactory();
        DocumentEditor editor = new DocumentEditor(factory);

        editor.setContent("abc");
        assertEquals("abc", editor.getContent());
        assertEquals("FakeDocument", editor.getDocumentType());

        editor.display();
        editor.save("ignored.txt");

        assertTrue(factory.lastCreated.displayCalled);
        assertTrue(factory.lastCreated.saveCalled);
    }

    private static class FakeDocumentFactory implements DocumentFactory {
        FakeDocument lastCreated;

        @Override
        public Document createDocument() {
            lastCreated = new FakeDocument();
            return lastCreated;
        }
    }

    private static class FakeDocument implements Document {
        private String content = "";
        boolean displayCalled = false;
        boolean saveCalled = false;

        @Override
        public void setContent(String text) {
            content = (text == null) ? "" : text;
        }

        @Override
        public String getContent() {
            return content;
        }

        @Override
        public void display() {
            displayCalled = true;
        }

        @Override
        public void save(String path) {
            saveCalled = true;
        }
    }
}
