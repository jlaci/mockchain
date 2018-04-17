package hu.jlaci.mockchain.block.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.jlaci.mockchain.block.Block;
import hu.jlaci.mockchain.block.BlockStorageService;
import hu.jlaci.mockchain.mining.MiningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class BlockListener {

    private ObjectMapper objectMapper;

    private BlockStorageService blockStorageService;

    private MiningService miningService;

    public BlockListener(ObjectMapper objectMapper, BlockStorageService blockStorageService, MiningService miningService) {
        this.objectMapper = objectMapper;
        this.blockStorageService = blockStorageService;
        this.miningService = miningService;
    }

    public void receiveMessage(String message) {
        try {
            Block block = objectMapper.readValue(message, Block.class);
            log.info("Received block {} storing", block);
            blockStorageService.storeBlock(block);
            miningService.assembleBlock();
        } catch (IOException e) {
            log.warn("Failed to deserialize message received on Block channel! {}", message);
        }
    }
}
