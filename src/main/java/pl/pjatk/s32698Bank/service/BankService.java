package pl.pjatk.s32698Bank.service;

import org.springframework.stereotype.Service;
import pl.pjatk.s32698Bank.model.Client;
import pl.pjatk.s32698Bank.model.TransactionResult;
import pl.pjatk.s32698Bank.model.TransactionStatus;
import pl.pjatk.s32698Bank.storage.ClientStorage;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class BankService {

    private final ClientStorage clientStorage;

    public BankService(ClientStorage clientStorage) {
        this.clientStorage = clientStorage;
    }

    public Client register(BigDecimal initialBalance) {
        if (initialBalance == null) {
            throw new IllegalArgumentException("Cannot set a null balance!!!");
        }
        Client client = new Client(null, initialBalance);
        return clientStorage.register(client);
    }

    public Optional<Client> getClient(Long id) {
        return clientStorage.findById(id);
    }

    public TransactionResult deposit(Long clientId, BigDecimal amount) {
        Optional<Client> clientOpt = clientStorage.findById(clientId);

        if (clientOpt.isEmpty()) {
            return new TransactionResult(TransactionStatus.DECLINED, BigDecimal.ZERO);
        }

        if (isAmountInvalid(amount)) {
            return new TransactionResult(TransactionStatus.DECLINED, clientOpt.get().getBalance());
        }

        Client client = clientOpt.get();
        BigDecimal newBalance = client.getBalance().add(amount);
        client.setBalance(newBalance);

        return new TransactionResult(TransactionStatus.ACCEPTED, newBalance);
    }

    public TransactionResult makeTransfer(Long clientId, BigDecimal amount) {
        Optional<Client> clientOpt = clientStorage.findById(clientId);

        if (clientOpt.isEmpty()) {
            return new TransactionResult(TransactionStatus.DECLINED, BigDecimal.ZERO);
        }

        Client client = clientOpt.get();

        if (isAmountInvalid(amount)) {
            return new TransactionResult(TransactionStatus.DECLINED, client.getBalance());
        }

        if (client.getBalance().compareTo(amount) < 0) {
            return new TransactionResult(TransactionStatus.DECLINED, client.getBalance());
        }

        BigDecimal newBalance = client.getBalance().subtract(amount);
        client.setBalance(newBalance);

        return new TransactionResult(TransactionStatus.ACCEPTED, newBalance);
    }

    private boolean isAmountInvalid(BigDecimal amount) {
        return amount == null
                || amount.compareTo(BigDecimal.ZERO) <= 0
                || amount.stripTrailingZeros().scale() > 2;
    }
}