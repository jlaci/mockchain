package hu.jlaci.mockchain.transaction.messaging;

import hu.jlaci.mockchain.block.Block;
import hu.jlaci.mockchain.configuration.RedisConfiguration;
import hu.jlaci.mockchain.transaction.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransactionNotificationService {

    private RedisTemplate<String, Object> redisTemplate;

    public TransactionNotificationService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void storeTransaction(Transaction transaction) {
        log.info("Notifying nodes about transaction we want to store {}", transaction);
        redisTemplate.convertAndSend(RedisConfiguration.TRANSACTIONS_CHANNEL, transaction);
    }
}
