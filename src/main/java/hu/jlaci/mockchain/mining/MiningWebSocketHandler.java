package hu.jlaci.mockchain.mining;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.jlaci.mockchain.block.Block;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class MiningWebSocketHandler extends TextWebSocketHandler implements MiningListener {

    private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    private ObjectMapper objectMapper;

    private MiningService miningService;

    public MiningWebSocketHandler(ObjectMapper objectMapper, MiningService miningService) {
        this.objectMapper = objectMapper;
        this.miningService = miningService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Client connected on WebSocket {}", session.getId());
        sessions.put(session.getId(), session);
        Block block = miningService.getMinedBlock();

        if (block != null) {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(block)));
        } else {
            log.info("No block mined");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Client disconected from on WebSocket {}", session.getId());
        sessions.remove(session.getId());
    }

    @Override
    public void blockChanged(Block block) {
        try {
            for (WebSocketSession session : sessions.values()) {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(block)));
            }
        } catch (IOException e) {
            log.warn("Failed to notify clients of block change!", e);
        }
    }
}
