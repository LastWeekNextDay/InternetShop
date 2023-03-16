package lt.viko.eif.nlavkart.internetshop.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {
    @Test
    void creation() {
        Cart cart = new Cart();
        cart.setId(1);
        assertNotNull(cart);
        assertEquals(1, cart.getId());
    }
}