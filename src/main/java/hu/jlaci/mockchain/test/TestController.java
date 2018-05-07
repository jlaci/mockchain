package hu.jlaci.mockchain.test;

import hu.jlaci.mockchain.mining.MiningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    private MiningService miningService;

    public TestController(MiningService miningService) {
        this.miningService = miningService;
    }

    @PostMapping("/reset")
    public ResponseEntity resetChain() {
        log.info("Resetting chain");
        miningService.reset();
        return ResponseEntity.ok().build();
    }
}
