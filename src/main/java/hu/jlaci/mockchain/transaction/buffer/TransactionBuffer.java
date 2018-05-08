package hu.jlaci.mockchain.transaction.buffer;

import hu.jlaci.mockchain.transaction.Transaction;
import hu.jlaci.mockchain.transaction.TransactionValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class TransactionBuffer {

    private boolean reset;

    private TransactionValidationService validationService;

    public TransactionBuffer(TransactionValidationService validationService) {
        this.validationService = validationService;
    }

    private Map<UUID, Transaction> transactions = new HashMap<>();

    private List<UUID> transactionOrder = new ArrayList<>();

    private List<TransactionListener> listeners = new ArrayList<>();

    public int addTransaction(Transaction transaction) {
        if (reset) {
            log.info("Reset in progress, discarding added transaction");
            return 0;
        }

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

    public void transactionMined(UUID id) {
        if (reset) {
            log.info("Reset in progress, discarding mined transaction");
            return;
        }

        transactions.remove(id);
        transactionOrder.remove(id);
    }

    public List<Transaction> getFirst(int number) {
        if (reset) {
            log.info("Reset in progress, returning emtpy list.");
            return Collections.emptyList();
        }

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

    public void reset() {
        log.warn("Resetting chain!");
        reset = true;
        transactions.clear();
        transactionOrder.clear();
        reset = false;
    }
}
