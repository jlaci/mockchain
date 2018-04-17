package hu.jlaci.mockchain.transaction;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TransactionValidationService {

    private Map<UUID, byte[]> signatures;

    public TransactionValidationService() {
        signatures = new HashMap<>();
        signatures.put(UUID.fromString("c7f65582-0970-43e8-8108-2d4d82d277ab"), new byte[] {0x1});
        signatures.put(UUID.fromString("c06ab28c-d13d-4ea6-8481-0dbc8a79c992"), new byte[] {0x2});
        signatures.put(UUID.fromString("f1042a75-baf9-425c-8c88-ee24d9e1687f"), new byte[] {0x3});
    }

    public boolean signatureValid(Transaction transaction) {
        if (signatures.containsKey(transaction.getOriginator())) {
            return Arrays.equals(signatures.get(transaction.getOriginator()), transaction.getSignature());  //TODO: real signature checking
        } else {
            return false;
        }
    }
}
