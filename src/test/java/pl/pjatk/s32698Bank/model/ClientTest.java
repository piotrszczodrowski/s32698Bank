package pl.pjatk.s32698Bank.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void shouldThrowExceptionWhenCreatingWithNullBalance() {
        assertThrows(IllegalArgumentException.class, () -> new Client(1L, null));
    }

    @Test
    void shouldThrowExceptionWhenCreatingWithTooManyDecimalPlaces() {
        assertThrows(IllegalArgumentException.class, () -> new Client(1L, new BigDecimal("100.123")));
    }

    @Test
    void shouldThrowExceptionWhenSettingNullBalance() {
        Client client = new Client(1L, BigDecimal.TEN);

        assertThrows(IllegalArgumentException.class, () -> client.setBalance(null));
    }

    @Test
    void shouldCreateClientWithValidBalance() {
        BigDecimal validBalance = new BigDecimal("100.120");

        Client client = new Client(1L, validBalance);

        assertNotNull(client);
        assertEquals(validBalance, client.getBalance());
    }
}