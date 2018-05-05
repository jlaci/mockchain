package hu.jlaci.mockchain.transaction.buffer;

import hu.jlaci.mockchain.transaction.Transaction;
import hu.jlaci.mockchain.transaction.TransactionValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class TransactionBuffer {

    private TransactionValidationService validationService;

    public TransactionBuffer(TransactionValidationService validationService) {
        this.validationService = validationService;
    }

    private Map<UUID, Transaction> transactions = new HashMap<>();

    private List<UUID> transactionOrder = new ArrayList<>();

    private List<TransactionListener> listeners = new ArrayList<>();

    public synchronized int addTransaction(Transaction transaction) {
        if (validationService.signatureValid(transaction)) {
            log.info("Got valid transaction {}, storing and notifying listeners", transaction);
            transactions.put(transaction.getTransactionId(), transaction);
            transactionOrder.add(transaction.getTransactionId());

            for (TransactionListener transactionListener: listeners) {
                transactionListener.receivedNewTransaction(transaction);
            }

        } else {
            log.warn("Got invalid transaction {}", transaction);
        }

        return transactions.size();
    }

    public synchronized void transactionMined(UUID id) {
        transactions.remove(id);
        transactionOrder.remove(id);
    }

    public synchronized List<Transaction> getFirst(int number) {
        int amount = Math.min(number, transactionOrder.size());
        List<Transaction> result = new ArrayList<>(amount);

        for (int i = 0; i < amount; i++) {
            result.add(transactions.get(transactionOrder.get(i)));
        }

        return result;
    }

    public void subscribe(TransactionListener listener) {
        listeners.add(listener);
    }

}
