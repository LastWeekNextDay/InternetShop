package lt.viko.eif.nlavkart.internetshop.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

        @Test
        void creation() {
            Item item = new Item();
            item.setId(1);
            item.setName("name");
            item.setDescription("description");
            item.setPrice(1.0);
            item.setQuantity(1);

            assertNotNull(item);
            assertEquals(1, item.getId());
            assertEquals("name", item.getName());
            assertEquals("description", item.getDescription());
            assertEquals(1.0, item.getPrice());
            assertEquals(1, item.getQuantity());
        }
}