package pl.pjatk.s32698Bank.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pjatk.s32698Bank.model.Client;
import pl.pjatk.s32698Bank.model.TransactionResult;
import pl.pjatk.s32698Bank.model.TransactionStatus;
import pl.pjatk.s32698Bank.storage.ClientStorage;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankServiceTest {

    @Mock
    private ClientStorage clientStorage;

    @InjectMocks
    private BankService bankService;

    @Test
    void shouldRegisterClient() {
        BigDecimal saldo = new BigDecimal("100");
        Client expectedClient = new Client(1L, saldo);
        when(clientStorage.register(any(Client.class))).thenReturn(expectedClient);

        Client result = bankService.register(saldo);

        assertEquals(saldo, result.getBalance());
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldDepositMoneySuccess() {
        Long id = 1L;
        Client client = new Client(id, new BigDecimal("100"));
        when(clientStorage.findById(id)).thenReturn(Optional.of(client));

        TransactionResult result = bankService.deposit(id, new BigDecimal("50"));

        assertEquals(TransactionStatus.ACCEPTED, result.getStatus());
        assertEquals(new BigDecimal("150"), result.getNewBalance());
    }

    @Test
    void shouldDeclineDepositIfClientNotFound() {
        Long id = 99L;
        when(clientStorage.findById(id)).thenReturn(Optional.empty());

        TransactionResult result = bankService.deposit(id, new BigDecimal("50"));

        assertEquals(TransactionStatus.DECLINED, result.getStatus());
    }

    @Test
    void shouldMakeTransferSuccess() {
        Long id = 1L;
        Client client = new Client(id, new BigDecimal("200"));
        when(clientStorage.findById(id)).thenReturn(Optional.of(client));

        TransactionResult result = bankService.makeTransfer(id, new BigDecimal("100"));

        assertEquals(TransactionStatus.ACCEPTED, result.getStatus());
        assertEquals(new BigDecimal("100"), result.getNewBalance());
    }

    @Test
    void shouldDeclineTransferNotEnoughFunds() {
        Long id = 1L;
        Client client = new Client(id, new BigDecimal("10"));
        when(clientStorage.findById(id)).thenReturn(Optional.of(client));

        TransactionResult result = bankService.makeTransfer(id, new BigDecimal("100"));

        assertEquals(TransactionStatus.DECLINED, result.getStatus());
        assertEquals(new BigDecimal("10"), result.getNewBalance());
    }

    @Test
    void shouldGetClientData() {
        Long id = 1L;
        Client client = new Client(id, new BigDecimal("500"));
        when(clientStorage.findById(id)).thenReturn(Optional.of(client));

        Optional<Client> result = bankService.getClient(id);

        assertTrue(result.isPresent());
        assertEquals(client, result.get());
    }

    @Test
    void shouldDeclineDepositWithInvalidAmount() {
        Long id = 1L;
        Client client = new Client(id, new BigDecimal("100"));
        when(clientStorage.findById(id)).thenReturn(Optional.of(client));

        TransactionResult resultNull = bankService.deposit(id, null);
        assertEquals(TransactionStatus.DECLINED, resultNull.getStatus());

        TransactionResult resultZero = bankService.deposit(id, BigDecimal.ZERO);
        assertEquals(TransactionStatus.DECLINED, resultZero.getStatus());

        TransactionResult resultNegative = bankService.deposit(id, new BigDecimal("-10"));
        assertEquals(TransactionStatus.DECLINED, resultNegative.getStatus());

        TransactionResult resultTooPrecise = bankService.deposit(id, new BigDecimal("10.001"));
        assertEquals(TransactionStatus.DECLINED, resultTooPrecise.getStatus());

        assertEquals(new BigDecimal("100"), client.getBalance());
    }
}