package hu.jlaci.mockchain.block.messaging;

import hu.jlaci.mockchain.block.Block;
import hu.jlaci.mockchain.configuration.RedisConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BlockNotificationService {

    private RedisTemplate<String, Object> redisTemplate;

    public BlockNotificationService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void blockMined(Block block) {
        log.info("Notifying nodes about block we mined {}", block);
        redisTemplate.convertAndSend(RedisConfiguration.BLOCKS_CHANNEL, block);
    }
}
