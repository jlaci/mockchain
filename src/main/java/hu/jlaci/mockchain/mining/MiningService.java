package hu.jlaci.mockchain.mining;

import hu.jlaci.mockchain.Protocol;
import hu.jlaci.mockchain.block.Block;
import hu.jlaci.mockchain.block.BlockStorageService;
import hu.jlaci.mockchain.block.BlockValidationService;
import hu.jlaci.mockchain.transaction.Transaction;
import hu.jlaci.mockchain.transaction.buffer.TransactionBuffer;
import hu.jlaci.mockchain.transaction.buffer.TransactionListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


//TODO: should introduce a credit for mining blocks, which can be used to insert transactions
@Service
@Slf4j
public class MiningService implements TransactionListener {

    private TransactionBuffer transactionBuffer;

    private BlockStorageService blockStorage;

    private BlockValidationService blockValidationService;

    private List<MiningListener> listeners;

    private boolean reset = false;

    public MiningService(TransactionBuffer transactionBuffer, BlockStorageService blockStorage, BlockValidationService blockValidationService) {
        this.transactionBuffer = transactionBuffer;
        this.blockStorage = blockStorage;
        this.blockValidationService = blockValidationService;
        this.listeners = new ArrayList<>();
    }

    @Getter
    private Block minedBlock;

    @PostConstruct
    public void init() {
        log.info("Initializing mining service");
        transactionBuffer.subscribe(this);
    }

    public void assembleBlock() {
        if (reset) {
            log.info("Reset in progress, discarding assemble block request");
            return;
        }

        List<Transaction> transactions = transactionBuffer.getFirst(Protocol.MAX_TRANSACTIONS_PER_BLOCK);
        if (transactions.size() == Protocol.MAX_TRANSACTIONS_PER_BLOCK) {
            log.info("Assembling new block to mine");
            Block block = new Block();
            block.setBlockId(UUID.randomUUID());
            block.setPreviousHash(blockStorage.getLastHash());
            block.setNonce(0);
            block.setTransactions(transactions);
            this.minedBlock = block;
            log.info("New block is {}", block.getBlockId());
            minedBlockChanged(block);
        } else {
            log.info("No enough transactions to assemble block");
            this.minedBlock = null;
        }

    }

    public void clientMindedBlock(Block block) {
        if (reset) {
            log.info("Reset in progress, discarding mined block");
            return;
        }

        if (blockValidationService.blockValid(block, blockStorage.getLastBlock())) {
            log.info("Client successfully mined block {}!", block.getBlockId());
            blockStorage.storeBlock(block);
            assembleBlock();
        }
    }

    private void minedBlockChanged(Block block) {
        if (reset) {
            log.info("Reset in progress");
            return;
        }

        log.info("Mined block changed, notifying clients!");
        for (MiningListener miningListener : listeners) {
            miningListener.blockChanged(block);
        }
    }

    public void registerListener(MiningListener miningListener) {
        listeners.add(miningListener);
    }

    @Override
    public void receivedNewTransaction(Transaction transaction) {
        if (reset) {
            log.info("Reset in progress, discarding new transaction");
        }

        if (minedBlock == null) {
            assembleBlock();
        }
    }

    public void reset() {
        log.warn("Resetting chain!");
        reset = true;
        minedBlock = null;
        blockStorage.reset();
        reset = false;
    }
}
