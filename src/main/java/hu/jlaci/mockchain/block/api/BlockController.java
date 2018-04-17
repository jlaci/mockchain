package hu.jlaci.mockchain.block.api;

import hu.jlaci.mockchain.block.BlockStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/block")
public class BlockController {

    private BlockStorageService blockStorageService;

    public BlockController(BlockStorageService blockStorageService) {
        this.blockStorageService = blockStorageService;
    }

    @GetMapping
    public ResponseEntity getBlockChain() {
        return ResponseEntity.ok(blockStorageService.getBlockChain());
    }
}
