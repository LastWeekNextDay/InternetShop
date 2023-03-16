package lt.viko.eif.nlavkart.internetshop.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {
    @Test
    void creation() {
        Category category = new Category(1, "name", "description");
        assertNotNull(category);
        assertEquals(1, category.getId());
        assertEquals("name", category.getName());
        assertEquals("description", category.getDescription());
    }

}