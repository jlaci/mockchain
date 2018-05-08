package hu.jlaci.mockchain.block;

import hu.jlaci.mockchain.transaction.Transaction;
import hu.jlaci.mockchain.transaction.buffer.TransactionBuffer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class BlockStorageService {

    private TransactionBuffer transactionBuffer;

    private BlockValidationService validationService;

    private boolean reset;

    public BlockStorageService(TransactionBuffer transactionBuffer, BlockValidationService validationService) {
        this.transactionBuffer = transactionBuffer;
        this.validationService = validationService;
    }

    @Getter
    private List<Block> blockChain = new ArrayList<>();

    @PostConstruct
    public void init() {
        Block genesisBlock = new Block();
        genesisBlock.setBlockId(UUID.fromString("50e9ae22-9c93-4e2b-a996-564964547fb7"));
        genesisBlock.setPreviousHash("");
        genesisBlock.setNonce(123456);
        blockChain.add(genesisBlock);
        log.info("Appending genesis block {}", genesisBlock);
    }

    public void storeBlock(Block block) {
        if (reset) {
            log.info("Reset in progress, discarding stored block");
            return;
        }

        if (validationService.blockValid(block, getLastBlock())) {
            log.info("Block is valid, appending");
            blockChain.add(block);

            for (Transaction transaction : block.getTransactions()) {
                transactionBuffer.transactionMined(transaction.getTransactionId());
            }
        } else {
            log.warn("Block is invalid, checking for fork!");
            for (Block existingBlock : blockChain) {
                if (existingBlock.getHash().equals(block.getPreviousHash())) {
                    log.warn("Fork detected");
                    //TODO: Handle fork
                    return;
                }
            }
            log.warn("No fork, block is invalid!");
        }
    }

    public Block getLastBlock() {
        return blockChain.get(blockChain.size() - 1);
    }

    public String getLastHash() {
        return getLastBlock().getHash();
    }

    public void reset() {
        log.warn("Resetting chain!");
        reset = true;
        Block genesisBlock = blockChain.get(0);
        blockChain.clear();
        blockChain.add(genesisBlock);
        transactionBuffer.reset();
        reset = false;
    }
}
