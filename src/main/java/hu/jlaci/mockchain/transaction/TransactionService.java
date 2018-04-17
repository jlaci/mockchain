package hu.jlaci.mockchain.transaction;

import hu.jlaci.mockchain.node.NodeConfig;
import hu.jlaci.mockchain.transaction.buffer.TransactionBuffer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class TransactionService {

    private NodeConfig nodeConfig;

    private TransactionBuffer transactionBuffer;

    public TransactionService(NodeConfig nodeConfig, TransactionBuffer transactionBuffer) {
        this.nodeConfig = nodeConfig;
        this.transactionBuffer = transactionBuffer;
    }

    public void createTestTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID());
        transaction.setOriginator(nodeConfig.getId());
        transaction.setMessage("Test transaction message " + transaction.getTransactionId());
        transaction.setSignature(new byte[] {(byte) nodeConfig.getSignature()});
        log.info("Created test transaction {}", transaction);
        transactionBuffer.addTransaction(transaction);
    }

}
