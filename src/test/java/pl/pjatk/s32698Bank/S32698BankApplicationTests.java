package pl.pjatk.s32698Bank;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pjatk.s32698Bank.model.Client;
import pl.pjatk.s32698Bank.model.TransactionResult;
import pl.pjatk.s32698Bank.model.TransactionStatus;
import pl.pjatk.s32698Bank.service.BankService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class S32698BankApplicationTests {

	@Autowired
	private BankService bankService;

	@Test
	void contextLoads() {
		assertNotNull(bankService);
	}

	@Test
	void shouldPerformFullScenarioIntegration() {
		Client client = bankService.register(new BigDecimal("500.00"));
		assertNotNull(client.getId());

		TransactionResult transferResult = bankService.makeTransfer(client.getId(), new BigDecimal("100.00"));
		assertEquals(TransactionStatus.ACCEPTED, transferResult.getStatus());
		assertEquals(new BigDecimal("400.00"), transferResult.getNewBalance());

		TransactionResult depositResult = bankService.deposit(client.getId(), new BigDecimal("50.00"));
		assertEquals(TransactionStatus.ACCEPTED, depositResult.getStatus());
		assertEquals(new BigDecimal("450.00"), depositResult.getNewBalance());

		Optional<Client> fetchedClient = bankService.getClient(client.getId());
		assertTrue(fetchedClient.isPresent());
		assertEquals(new BigDecimal("450.00"), fetchedClient.get().getBalance());
	}
}