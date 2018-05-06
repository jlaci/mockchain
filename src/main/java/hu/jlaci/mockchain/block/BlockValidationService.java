package hu.jlaci.mockchain.block;

import hu.jlaci.mockchain.Protocol;
import hu.jlaci.mockchain.transaction.Transaction;
import hu.jlaci.mockchain.transaction.TransactionValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BlockValidationService {

    private TransactionValidationService transactionValidationService;

    public BlockValidationService(TransactionValidationService transactionValidationService) {
        this.transactionValidationService = transactionValidationService;
    }

    public boolean blockValid(Block block, Block lastBlock) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Protocol.HARDNESS; i++) {
            sb.append("0");
        }

        if (block.getBlockId().equals(lastBlock.getBlockId())) {
            log.warn("Already mined block!");
            return false;
        }

        if (!block.getHash().startsWith(sb.toString())) {
            log.warn("Bad hash!");
            return false;
        }

        if (block.getBlockId() == null) {
            log.warn("Missing block id!");
            return false;
        }

        if (!block.getPreviousHash().equals(lastBlock.getHash())) {
            log.warn("Hash mismatch!");
            return false;
        }

        // Check the transactions
        for (Transaction transaction : block.getTransactions()) {
            if (!transactionValidationService.signatureValid(transaction)) {
                log.warn("Invalid transaction signature!");
                return false;
            }
        }

        return true;
    }
}
