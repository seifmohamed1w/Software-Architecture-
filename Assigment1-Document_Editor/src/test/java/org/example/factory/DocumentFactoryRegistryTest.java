// src/test/java/org/example/factory/DocumentFactoryRegistryTest.java
package org.example.factory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentFactoryRegistryTest {

    @Test
    void getFactory_shouldNormalizeInput() {
        assertNotNull(DocumentFactoryRegistry.getFactory(" PDF "));
        assertNotNull(DocumentFactoryRegistry.getFactory("WoRd"));
        assertNotNull(DocumentFactoryRegistry.getFactory("html"));
    }

    @Test
    void getFactory_shouldThrowForUnsupportedType() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> DocumentFactoryRegistry.getFactory("txt"));

        assertTrue(ex.getMessage().toLowerCase().contains("unsupported"));
        assertTrue(ex.getMessage().toLowerCase().contains("supported"));
    }

    @Test
    void getFactory_shouldThrowForNull() {
        assertThrows(IllegalArgumentException.class, () -> DocumentFactoryRegistry.getFactory(null));
    }
}
