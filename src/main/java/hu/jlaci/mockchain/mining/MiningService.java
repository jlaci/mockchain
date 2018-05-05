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
        log.info("Assembling new block to mine");
        Block block = new Block();
        block.setBlockId(UUID.randomUUID());
        block.setPreviousHash(blockStorage.getLastHash());
        block.setNonce(0);
        block.setTransactions(transactionBuffer.getFirst(Protocol.MAX_TRANSACTIONS_PER_BLOCK));
        this.minedBlock = block;
        minedBlockChanged(block);
    }

    public void clientMindedBlock(Block block) {
        if (blockValidationService.blockValid(block, blockStorage.getLastBlock())) {
            log.info("Client successfully mined block!");
            blockStorage.storeBlock(block);
            assembleBlock();
        }
    }

    public void minedBlockChanged(Block block) {
        log.info("Mined block changed, notifying clients!");
        for (MiningListener miningListener : listeners) {
            miningListener.blockChanged(block);
        }
    }

    @Override
    public void receivedNewTransaction(Transaction transaction) {
        if (minedBlock == null) {
            assembleBlock();
        } else if (minedBlock.getTransactions().size() < Protocol.MAX_TRANSACTIONS_PER_BLOCK) {
            minedBlock.getTransactions().add(transaction);
            minedBlockChanged(minedBlock);
        }
    }
}
