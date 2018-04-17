package hu.jlaci.mockchain.node;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NodeConfig {

    @Value("${mockchain.nodeId}")
    private String nodeId;

    @Getter
    @Value("${mockchain.signature}")
    private int signature;

    public UUID getId() {
        return UUID.fromString(nodeId);
    }

}
