package pl.pjatk.s32698Bank.storage;

import org.springframework.stereotype.Component;
import pl.pjatk.s32698Bank.model.Client;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ClientStorage {
    private final Map<Long, Client> clients = new HashMap<>();
    private Long nextId = 1L;

    public Client register(Client client) {
        client.setId(nextId);
        clients.put(nextId, client);
        nextId++;
        return client;
    }

    public Optional<Client> findById(Long id) {
        return Optional.ofNullable(clients.get(id));
    }
}