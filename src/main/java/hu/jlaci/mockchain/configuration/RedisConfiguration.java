package hu.jlaci.mockchain.configuration;

import hu.jlaci.mockchain.transaction.messaging.TransactionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfiguration {

    public static final String TRANSACTIONS_CHANNEL = "Transactions";

    public static final String BLOCKS_CHANNEL = "Blocks";


    /*@Bean
    JedisConnectionFactory connectionFactory(@Value("${network.redis.host}") String redisHost, @Value("${network.redis.port}") int redisPort) {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setHostName(redisHost);
        connectionFactory.setPort(redisPort);
        return connectionFactory;
    }*/


    @Bean
    RedisMessageListenerContainer transactionContainer(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(TRANSACTIONS_CHANNEL));
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(TransactionListener transactionListener) {
        return new MessageListenerAdapter(transactionListener, "receiveMessage");
    }

    @Bean
    RedisTemplate<String, Object> template(RedisConnectionFactory connectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

}
