package hu.jlaci.mockchain.transaction.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.jlaci.mockchain.transaction.Transaction;
import hu.jlaci.mockchain.transaction.buffer.TransactionBuffer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class TransactionListener {

    private ObjectMapper objectMapper;

    private TransactionBuffer transactionBuffer;

    public TransactionListener(ObjectMapper objectMapper, TransactionBuffer transactionBuffer) {
        this.objectMapper = objectMapper;
        this.transactionBuffer = transactionBuffer;
    }

    public void receiveMessage(String message) {
        try {
            Transaction transaction = objectMapper.readValue(message, Transaction.class);
            log.info("Received transaction {} adding to buffer", transaction);
            transactionBuffer.addTransaction(transaction);
        } catch (IOException e) {
            log.warn("Failed to deserialize message received on Transaction channel! {}", message);
        }
    }
}
