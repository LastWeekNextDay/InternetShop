package lt.viko.eif.nlavkart.internetshop.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    @Test
    void creation() {
        Account account = new Account(1, "username", "password", new Cart());
        assertNotNull(account);
        assertEquals(1, account.getId());
        assertEquals("username", account.getUsername());
        assertEquals("password", account.getPassword());
    }
}