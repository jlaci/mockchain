package hu.jlaci.mockchain.mining;

import hu.jlaci.mockchain.block.Block;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mining")
public class MiningController {

    private MiningService miningService;

    public MiningController(MiningService miningService) {
        this.miningService = miningService;
    }

    @GetMapping("mined-block")
    public ResponseEntity getBlockToMine() {
        Block block = miningService.getMinedBlock();

        if (block == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(block);
        }
    }

    @PostMapping("mined-block")
    public ResponseEntity minedBlock(@RequestBody Block block) {
        miningService.clientMindedBlock(block);
        return ResponseEntity.noContent().build();
    }

}
