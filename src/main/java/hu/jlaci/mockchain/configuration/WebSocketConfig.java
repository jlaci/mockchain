package hu.jlaci.mockchain.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.jlaci.mockchain.mining.MiningService;
import hu.jlaci.mockchain.mining.MiningWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	private ObjectMapper objectMapper;

	private MiningService miningService;

	public WebSocketConfig(ObjectMapper objectMapper, MiningService miningService) {
		this.objectMapper = objectMapper;
		this.miningService = miningService;
	}

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new MiningWebSocketHandler(objectMapper, miningService), "/block-changed").setAllowedOrigins("*");
	}

}